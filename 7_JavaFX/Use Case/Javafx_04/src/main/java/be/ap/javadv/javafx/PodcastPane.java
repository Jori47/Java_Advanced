package be.ap.javadv.javafx;

import be.ap.javadv.javafx.style.ClockStyle;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.util.Callback;

public class PodcastPane extends Group {
	private final Podcast defaultPodcast = new Podcast("<Podcast title>",
			"https://static.thenounproject.com/png/29022-200.png", "<url of Audio>");

	ObservableList<Podcast> data;
	BooleanProperty showAddPodcast = new SimpleBooleanProperty();

	public PodcastPane(ObservableList<Podcast> data) {
		this.data = FXCollections.observableArrayList(data);

		getChildren().addAll(podcastContent());
	}

	// TODO: 1. Observe structure of Podcastpane
	public Node podcastContent() {
		VBox vBox = new VBox();

		vBox.setPadding(new Insets(ClockStyle.SPACING, ClockStyle.SPACING, ClockStyle.SPACING, ClockStyle.DIAM / 2));
		vBox.setMinWidth(ClockStyle.DIAM * 2 - 2 * ClockStyle.SPACING);
		vBox.setMinHeight(ClockStyle.DIAM - 2 * ClockStyle.SPACING);
		vBox.setMaxHeight(ClockStyle.DIAM - 2 * ClockStyle.SPACING);
		vBox.setAlignment(Pos.TOP_LEFT);

		// TODO: 2. See how CSS is now supported
		vBox.setStyle("-fx-background-color: rgb(192, 0, 0);");
		Node podcastListViewVar = podcastListView();
		VBox.setVgrow(podcastListViewVar, Priority.ALWAYS);
		vBox.getChildren().setAll(titleBox(), spacer(), podcastListViewVar);

		return vBox;
	}

	public Node titleBox() {
		HBox titleBox = new HBox(ClockStyle.SPACING);
		Pane spacer = new Pane();
		Font font = new Font("Arial", 24);
		Label title = new Label("Podcasts");
		Label plusSign = new Label("+");

		titleBox.setAlignment(Pos.BASELINE_LEFT);
		HBox.setHgrow(spacer, Priority.ALWAYS);

		title.setFont(font);
		title.setTextFill(Color.WHITE);

		plusSign.setFont(font);
		plusSign.setTextFill(Color.WHITE);
		plusSign.setOnMouseClicked(e -> {
			data.add(defaultPodcast);
		});

		titleBox.getChildren().addAll(title, spacer, plusSign);

		return titleBox;
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

	public Node podcastListView() {
		// TODO: 3. Creating a listview
		final ListView<Podcast> podCastListView = new ListView<Podcast>(this.data);

		podCastListView.setCellFactory(new Callback<ListView<Podcast>, ListCell<Podcast>>() {
			@Override
			public ListCell<Podcast> call(ListView<Podcast> listView) {
				return new PodcastCell();
			}
		});

		// TODO: 4. Comparing styles
		// podCastListView.setStyle("-fx-background-color: rgb(192, 0, 0);");
		podCastListView.setBackground(new Background(
			new BackgroundFill(ClockStyle.ACCENT, CornerRadii.EMPTY, Insets.EMPTY)));
		podCastListView.setStyle(".list-view .scroll-bar:horizontal {-fx-opacity: 0; -fx-padding:-7; }");

		return podCastListView;
	}
}