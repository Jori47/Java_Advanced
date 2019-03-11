package be.ap.javadv.javafx;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Timesup extends Application {
	private static Stage stage;

	public static Stage getStage() {
		return stage;
	}

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage stage) {
		Timesup.stage = stage;

		Group clock = new Clock();
		Scene scene = new Scene(clock);
		Image icon = new Image("be/ap/javadv/javafx/img/Timesup.png");

		scene.setFill(Color.TRANSPARENT);
		stage.setScene(scene);
		stage.initStyle(StageStyle.TRANSPARENT);
		stage.getIcons().add(icon);
		stage.show();
	}
}