package be.ap.javadv.javafx;

import be.ap.javadv.javafx.style.ClockStyle;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class Clock extends Group {
	private static double[] SCRN;
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

		clock.getChildren().addAll(createDial());
		clock.setOnMouseClicked(e -> {
			if (e.getButton().equals(MouseButton.PRIMARY) && e.getClickCount() == 2) {
				moveStageToNextPos();
			}
		});

		return clock;
	}

	private Node createDial() {
		Group border = new Group();

		border.getChildren().addAll(createOuterBorder(), createLogo());

		return border;
	}

	private Node createOuterBorder() {
		Circle border = new Circle(ClockStyle.RADIUS, ClockStyle.RADIUS, ClockStyle.RADIUS);
		border.setFill(Color.WHITE);
		border.setStrokeWidth(.7);

		return border;
	}

	private Node createLogo() {
		Image brand = new Image("be/ap/javadv/javafx/img/AP.png");
		ImageView view = new ImageView();
		view.setImage(brand);
		view.setPreserveRatio(true);
		view.setTranslateX(ClockStyle.RADIUS - ClockStyle.SPACING);
		view.setTranslateY(ClockStyle.RADIUS * ClockStyle.LOGO_REL_POS);
		view.setFitWidth(ClockStyle.LOGO_WIDTH);

		view.setOnMouseClicked(e -> {
			sidePaneVisible.set(!sidePaneVisible.getValue());
		});

		return view;
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