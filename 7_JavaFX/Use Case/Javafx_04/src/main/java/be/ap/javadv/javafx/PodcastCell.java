package be.ap.javadv.javafx;

import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;

import be.ap.javadv.javafx.style.ClockStyle;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class PodcastCell extends ListCell<Podcast> {
	private VBox content;
	private ImageView image;
	private Text title;
	private Text url;
	private MediaView playerView;

	public PodcastCell() {
		super();

		this.image = new ImageView();
		this.image.setFitHeight(ClockStyle.LISTCELL_HEIGHT);
		this.image.setFitWidth(ClockStyle.LISTCELL_HEIGHT);
		this.image.setStyle("-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.8), 10, 0, 0, 0);");
		this.image.setOnMouseClicked(e -> {
			if (e.getButton().equals(MouseButton.SECONDARY)) {
				getItem().setImage(fromClipboard());
				updateItem(getItem(), false);
			} else if (e.getButton().equals(MouseButton.PRIMARY)) {
				playAudio(getItem().getUrl());
			}
		});

		MediaPlayer player = new MediaPlayer(new Media(
				"file:///C:/Users/David/Music/11_De_klank_van_missen_3906455_2017_06_01T10_46_31_301_ondemand_128.mp3"));
		this.playerView = new MediaView(player);

		this.title = new Text();
		this.title.setFill(Color.WHITE);
		this.title.setFont(Font.font("Arial", FontWeight.BOLD, 16));
		this.title.setWrappingWidth(ClockStyle.DIAM);
		this.title.setOnMouseClicked(e -> {
			if (e.getButton().equals(MouseButton.SECONDARY)) {
				getItem().setTitle(fromClipboard());
				updateItem(getItem(), false);
			}
		});

		this.url = new Text();
		this.url.setFill(Color.WHITE);
		this.url.setFont(Font.font("Arial", FontPosture.ITALIC, 14));
		this.url.setWrappingWidth(ClockStyle.DIAM);
		this.url.setOnMouseClicked(e -> {
			if (e.getButton().equals(MouseButton.SECONDARY)) {
				getItem().setUrl(fromClipboard());
				updateItem(getItem(), false);
			}
		});

		VBox description = new VBox(this.title, this.url);
		description.setSpacing(10);
		description.setPadding(new Insets(0, 0, 0, 10));
		description.setMaxWidth(ClockStyle.DIAM);
		description.setMinWidth(ClockStyle.DIAM);

		HBox cell = new HBox(this.image, playerView, description);

		content = new VBox(cell, spacer());
		content.setPadding(new Insets(0));
		content.setStyle("-fx-background-color: rgb(192, 0, 0);");

		this.setPadding(new Insets(0));
		this.setMaxWidth(2 * ClockStyle.DIAM - 2 * ClockStyle.SPACING);
	}

	public Node spacer() {
		BorderPane spacerPane = new BorderPane();
		Line spacerLine = new Line(0, 0, ClockStyle.DIAM * 1.4, 0);
		
		spacerLine.setStroke(Color.RED);
		spacerLine.setStrokeWidth(2);
		spacerLine.setLayoutY(ClockStyle.SPACING);
		spacerPane.setMinHeight(ClockStyle.SPACING * 2);
		spacerPane.setPadding(new Insets(ClockStyle.SPACING));
		spacerPane.getChildren().add(spacerLine);

		return spacerPane;
	}

	@Override
	protected void updateItem(Podcast item, boolean empty) {
		super.updateItem(item, empty);
		if (item != null && !empty) {
			this.title.setText(item.getTitle());
			this.url.setText(item.getUrl());
			this.image.setImage(new Image(item.getImage()));

			setGraphic(content);
		} else {
			setGraphic(null);
		}
	}

	private void playAudio(String audioUrl) {
		System.out.println("playing " + audioUrl);

		this.playerView.setMediaPlayer(new MediaPlayer(new Media(audioUrl)));
		this.playerView.getMediaPlayer().play();
	}

	public String fromClipboard() {
		String data;

		try {
			data = (String) Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor);
		} catch (Exception e) {
			data = "<Copy from Clipboard failed>";
		}

		return data;
	}
}