package be.ap.javadv.javafx;

import java.util.ArrayList;

import be.ap.javadv.javafx.style.ClockStyle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class SidePane extends Group {
	public SidePane() {
		getChildren().addAll(createSidePane());
	}

	public Node createSidePane() {
		StackPane sidePane = new StackPane();
		Stage stage = Timesup.getStage();

		sidePane.setPadding(new Insets(ClockStyle.SPACING, ClockStyle.SPACING, ClockStyle.SPACING, ClockStyle.RADIUS));
		sidePane.visibleProperty().bind(Clock.sidePaneVisible);

		Clock.sidePaneVisible.addListener((arg, oldVal, newVal) -> {
			if (newVal) {
				stage.setWidth(ClockStyle.RADIUS + ClockStyle.PANE_WIDTH);

			} else {
				stage.setWidth(ClockStyle.DIAM);
			}

			Clock.setPos(Clock.getPos());
		});

		ArrayList<Podcast> podcasts = new ArrayList<>();

		podcasts.add(new Podcast("Nieuwe feiten 8 maart", "https://cds.radio1.be/sites/default/files/styles/800x800/public/article/2018_10/logo_nieuwe_feiten_2.jpg", "https://progressive-audio.lwc.vrtcdn.be/content/2019/03/08/11_De_Nieuwe_Feiten_van_8_03_2019_5204415_2019_03_08T14_09_27_341_ondemand_128.mp3"));
		podcasts.add(new Podcast("Nieuwe feiten 7 maart", "https://cds.radio1.be/sites/default/files/styles/800x800/public/article/2018_10/logo_nieuwe_feiten_2.jpg", "https://progressive-audio.lwc.vrtcdn.be/content/2019/03/07/11_De_Nieuwe_Feiten_van_07_03_2019_5201705_2019_03_07T14_29_16_043_ondemand_128.mp3"));
		podcasts.add(new Podcast("Nieuwe feiten 6 maart", "https://cds.radio1.be/sites/default/files/styles/800x800/public/article/2018_10/logo_nieuwe_feiten_2.jpg", "https://progressive-audio.lwc.vrtcdn.be/content/2019/03/06/11_De_Nieuwe_Feiten_van_06_03_2019_5198516_2019_03_06T14_25_35_659_ondemand_128.mp3"));
		podcasts.add(new Podcast("Nieuwe feiten 5 maart", "https://cds.radio1.be/sites/default/files/styles/800x800/public/article/2018_10/logo_nieuwe_feiten_2.jpg", "https://progressive-audio.lwc.vrtcdn.be/content/2019/03/05/11_De_Nieuwe_Feiten_van_05_03_2019_5195873_2019_03_05T14_23_16_081_ondemand_128.mp3"));

		ObservableList<Podcast> data = FXCollections.observableArrayList(podcasts);

		sidePane.getChildren().addAll(createSidePaneBack(), new PodcastPane(data));

		return sidePane;
	}

	public Node createSidePaneBack() {
		Rectangle background = new Rectangle(0, 0, ClockStyle.PANE_WIDTH - ClockStyle.SPACING,
				ClockStyle.DIAM - 2 * ClockStyle.SPACING);

		background.setFill(ClockStyle.ACCENT);
		background.setArcWidth(ClockStyle.ARC);
		background.setArcHeight(ClockStyle.ARC);

		return background;
	}
}