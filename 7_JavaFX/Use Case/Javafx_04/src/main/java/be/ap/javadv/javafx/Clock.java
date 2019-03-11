package be.ap.javadv.javafx;

import be.ap.javadv.javafx.style.ClockStyle;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableDoubleValue;
import javafx.beans.value.ObservableStringValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.SVGPath;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Scale;
import javafx.scene.transform.Translate;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Clock extends Group {
	private static double[] SCRN;
	private static final Duration SECONDS_HAND_TICK = Duration.millis(50);
	private static final Duration MINUTE_HAND_TICK = Duration.millis(500);
	private static final Duration HOUR_HAND_TICK = Duration.seconds(10);
	private static final double HOURS_ON_CLOCK = 12d;
	private static final double SECONDS_ON_CLOCK = 60d;
	private static final double MINUTES_ON_CLOCK = 60d;

	private static final double DEGREES_PER_SECOND = 360d / SECONDS_ON_CLOCK;
	private static final double DEGREES_PER_MINUTE = 360d / MINUTES_ON_CLOCK;
	private static final double DEGREES_PER_HOUR = 360d / HOURS_ON_CLOCK;

	private SimpleDoubleProperty hrsAngle = new SimpleDoubleProperty(0);
	private SimpleDoubleProperty minAngle = new SimpleDoubleProperty(0);
	private SimpleDoubleProperty secAngle = new SimpleDoubleProperty(0);

	private SimpleStringProperty weekDay = new SimpleStringProperty("weekday");
	private SimpleStringProperty mnthDay = new SimpleStringProperty("00");
	private SimpleStringProperty mnthNam = new SimpleStringProperty("monthname");

	private static Position pos = Position.TOP_RIGHT;
	public static BooleanProperty sidePaneVisible = new SimpleBooleanProperty(false);

	static {
		Rectangle2D scrBounds = Screen.getPrimary().getVisualBounds();
		Clock.SCRN = new double[] { scrBounds.getWidth(), scrBounds.getHeight() };
	}

	public Clock() {
		Clock.setPos(Position.TOP_RIGHT);

		getChildren().addAll(new SidePane(), createClock());
	}

	public static Position getPos() {
		return pos;
	}

	public static void setPos(Position newPos) {
		Stage stage = Timesup.getStage();
		double newX = 0;
		double newY = 0;

		switch (newPos) {
		case TOP_RIGHT:
			newX = Clock.SCRN[0] - ClockStyle.DIAM;
			newY = 0;
			break;
		case BOTTOM_RIGHT:
			newX = Clock.SCRN[0] - ClockStyle.DIAM;
			newY = Clock.SCRN[1] - ClockStyle.DIAM;
			break;
		case BOTTOM_LEFT:
			newX = 0;
			newY = Clock.SCRN[1] - ClockStyle.DIAM;
			break;
		case TOP_LEFT:
			newX = 0;
			newY = 0;
			break;

		default:
			break;
		}

		if (Clock.sidePaneVisible.getValue() && (newPos == Position.TOP_RIGHT || newPos == Position.BOTTOM_RIGHT)) {
			newX = newX - ClockStyle.PANE_WIDTH + ClockStyle.RADIUS;
		}

		stage.setX(newX);
		stage.setY(newY);

		pos = newPos;
	}

	public Node createClock() {
		Group clock = new Group();

		clock.getChildren().addAll(createDial(), createDate(), createHands());
		clock.setOnMouseClicked(e -> {
			if (e.getButton().equals(MouseButton.PRIMARY) && e.getClickCount() == 2) {
				moveStageToNextPos();
			}
		});

		updateMinuteHandAngle();
		updateHourHandAngle();
		updateDate();

		startTicking();

		return clock;
	}

	private Node createDial() {
		Group border = new Group();

		border.getChildren().addAll(createOuterBorder(), createInnerBorder(), createGradient(), createTicks(),
				createLogo());

		return border;
	}

	private Node createOuterBorder() {
		Circle border = new Circle(ClockStyle.RADIUS, ClockStyle.RADIUS, ClockStyle.RADIUS);
		border.setFill(Color.WHITE);
		border.setStrokeWidth(.7);

		return border;
	}

	private Node createInnerBorder() {
		Circle border = new Circle(ClockStyle.RADIUS, ClockStyle.RADIUS, ClockStyle.RADIUS * .98);
		border.setStroke(Color.WHITE);
		border.setStrokeWidth(3);

		return border;
	}

	private Node createGradient() {
		Stop[] gradStops = new Stop[] { new Stop(0.85, Color.WHITE), new Stop(1, Color.GRAY) };
		RadialGradient gradFill = new RadialGradient(0, 0, .5, .5, .5, true, CycleMethod.NO_CYCLE, gradStops);
		Circle gradient = new Circle(ClockStyle.RADIUS, ClockStyle.RADIUS, ClockStyle.RADIUS * .97, gradFill);

		return gradient;
	}

	private Node createTicks() {
		Group ticks = new Group();
		ticks.getChildren().addAll(createMinuteTicks(), createHourTicks());
		return ticks;
	}

	private Node createLogo() {
		Image logo = new Image("be/ap/javadv/javafx/img/AP.png");
		ImageView view = new ImageView();
		double width = ClockStyle.LOGO_WIDTH / 300.0 * ClockStyle.DIAM;

		view.setImage(logo);
		view.setPreserveRatio(true);
		view.setTranslateX(ClockStyle.RADIUS - width / 2.0);
		view.setTranslateY(ClockStyle.RADIUS * ClockStyle.LOGO_REL_POS);
		view.setFitWidth(width);

		view.setOnMouseClicked(e -> {
			sidePaneVisible.set(!sidePaneVisible.getValue());
		});

		return view;
	}

	private Node createDate() {
		Group date = new Group();

		double size = 12.0 / 300.0 * ClockStyle.DIAM;
		double shift = 20.0 / 12.0 * size;

		Label mnthNam = createDatText(this.weekDay, ClockStyle.RADIUS / 3 * 2 - shift, size);
		Label weekDay = createDatText(this.mnthDay, ClockStyle.RADIUS / 3 * 2, size * 3);
		Label mnthDay = createDatText(this.mnthNam, ClockStyle.RADIUS / 3 * 2 + shift, size);

		date.getChildren().addAll(weekDay, mnthDay, mnthNam);

		date.setOnMouseClicked(e -> {
			System.out.println("Clicked on calendar");
		});

		return date;
	}

	private Label createDatText(ObservableStringValue txtProp, double yPos, double size) {
		Label label = new Label("Test");
		Font font = new Font("Arial", size);

		label.setTextAlignment(TextAlignment.CENTER);
		label.setFont(font);
		label.setStyle("-fx-font-weight: bold;");

		label.textProperty().bind(txtProp);

		label.translateXProperty().bind(label.widthProperty().divide(2).negate().add(ClockStyle.RADIUS));
		label.translateYProperty().bind(label.heightProperty().divide(2).negate().add(yPos));

		return label;
	}

	private Node createMinuteTicks() {
		Group minuteTicks = new Group();
		for (int n = 0; n < 60; n++) {
			if (n % 5 != 0) {
				double angle = 360 / 60 * n;
				minuteTicks.getChildren().add(createTickMark(1, angle));
			}
		}

		return minuteTicks;
	}

	private Node createHourTicks() {
		Group hourTicks = new Group();
		for (int n = 0; n < 12; n++) {
			double angle = 360 / 12 * n;
			hourTicks.getChildren().add(createTickMark(8, angle));
		}

		return hourTicks;
	}

	private Node createTickMark(double width, double angle) {
		Line tick = new Line();
		tick.setStartX(ClockStyle.RADIUS);
		tick.setStartY(ClockStyle.RADIUS * .1);
		tick.setEndX(ClockStyle.RADIUS);
		tick.setEndY(ClockStyle.RADIUS * .25);

		tick.getTransforms().addAll(new Rotate(angle, ClockStyle.RADIUS, ClockStyle.RADIUS));
		tick.setStrokeWidth(width);

		return tick;
	}

	private Node createHands() {
		Group hands = new Group();
		hands.getChildren()
				.addAll(createHand("MinutHand", getHandPath("SimpleHand"), Color.BLACK, Color.TRANSPARENT, 1, minAngle,
						.55 / 300 * ClockStyle.DIAM),
						createHand("HoursHand", getHandPath("SimpleHand2"), Color.BLACK, Color.WHITE, 1, hrsAngle,
								.55 / 300 * ClockStyle.DIAM),
						createHand("SecndHand", getHandPath("SecondHand"), Color.rgb(192, 0, 0), Color.rgb(192, 0, 0),
								3, secAngle, .6 / 300 * ClockStyle.DIAM));

		return hands;
	}

	private Node createHand(String name, String path, Color fill, Color border, int borderThickness,
			ObservableDoubleValue angle, double scale) {

		SVGPath handPath = new SVGPath();

		handPath.setContent(path);
		handPath.setFill(fill);
		handPath.setStrokeWidth(borderThickness);
		handPath.setStroke(border);

		Scale scaling = new Scale(scale, scale);
		Translate translation = new Translate(ClockStyle.RADIUS, ClockStyle.RADIUS);
		Rotate rotation = new Rotate(angle.doubleValue());

		rotation.angleProperty().bind(angle);

		handPath.getTransforms().setAll(translation, scaling, rotation);
		handPath.setId(name);

		return handPath;
	}

	private String getHandPath(String name) {
		String filename = String.format("src/main/java/be/ap/javadv/javafx/style/%s.txt", name);
		String path = "";

		try {
			byte[] encoded;
			encoded = Files.readAllBytes(Paths.get(filename));
			path = new String(encoded, Charset.defaultCharset());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return path;
	}

	private void startTicking() {
		startHandTicking(SECONDS_HAND_TICK, e -> {
			updateSecondsHandAngle();
		});
		startHandTicking(MINUTE_HAND_TICK, e -> {
			updateMinuteHandAngle();
		});
		startHandTicking(HOUR_HAND_TICK, e -> {
			updateHourHandAngle();
		});
	}

	private void startHandTicking(Duration tickDuration, EventHandler<ActionEvent> onTick) {
		Timeline tl = new Timeline();
		tl.setCycleCount(Timeline.INDEFINITE);
		tl.getKeyFrames().add(new KeyFrame(tickDuration, onTick));
		tl.play();
	}

	private void updateSecondsHandAngle() {
		secAngle.set(currentSecondsWithFractions() * DEGREES_PER_SECOND);
	}

	private void updateMinuteHandAngle() {
		minAngle.set(currentMinutesWithFractions() * DEGREES_PER_MINUTE);
	}

	private void updateHourHandAngle() {
		hrsAngle.set(currentHourWithFractions() * DEGREES_PER_HOUR);
		updateDate();
	}

	private void updateDate() {
		Date dat = new Date();
		DateFormat mnthDayFormat = new SimpleDateFormat("dd");
		DateFormat weekDayFormat = new SimpleDateFormat("EEEE");
		DateFormat mnthNamFormat = new SimpleDateFormat("MMMM");

		this.mnthDay.set(mnthDayFormat.format(dat));
		this.weekDay.set(weekDayFormat.format(dat));
		this.mnthNam.set(mnthNamFormat.format(dat));
	}

	private double currentSecondsWithFractions() {
		Calendar calendar = Calendar.getInstance();
		double currentSeconds = calendar.get(Calendar.SECOND);
		double currentMilliseconds = calendar.get(Calendar.MILLISECOND);
		return currentSeconds + currentMilliseconds / 1000d;
	}

	private double currentMinutesWithFractions() {
		Calendar calendar = Calendar.getInstance();
		double currentMinutes = calendar.get(Calendar.MINUTE);
		double currentSeconds = calendar.get(Calendar.SECOND);
		return currentMinutes + currentSeconds / 60d;
	}

	private double currentHourWithFractions() {
		Calendar calendar = Calendar.getInstance();
		double currentHour = calendar.get(Calendar.HOUR);
		double currentMinutes = calendar.get(Calendar.MINUTE);
		return currentHour + currentMinutes / 60d;
	}

	private void moveStageToNextPos() {
		switch (Clock.getPos()) {
		case TOP_RIGHT:
			Clock.setPos(Position.BOTTOM_RIGHT);
			break;
		case BOTTOM_RIGHT:
			Clock.setPos(Position.BOTTOM_LEFT);
			break;
		case BOTTOM_LEFT:
			Clock.setPos(Position.TOP_LEFT);
			break;
		case TOP_LEFT:
			Clock.setPos(Position.TOP_RIGHT);
			break;

		default:
			break;
		}
	}
}