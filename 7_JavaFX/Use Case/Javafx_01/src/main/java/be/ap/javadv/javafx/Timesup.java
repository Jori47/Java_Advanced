package be.ap.javadv.javafx;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Timesup extends Application {
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage stage) {
		Font font = new Font ("Arial", 72);
		Label text = new Label("TRANSPARENT");
		text.setFont(font);
		text.setTextFill(Color.WHITE);
		Group group = new Group(text);
		Scene scene = new Scene(group);

		scene.setFill(Color.TRANSPARENT);
		stage.setScene(scene);
		stage.initStyle(StageStyle.TRANSPARENT);
		stage.centerOnScreen();
		stage.show();
	}
}