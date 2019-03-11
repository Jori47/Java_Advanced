package be.ap.javadv.javafx;

import be.ap.javadv.javafx.style.ClockStyle;
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
		sidePane.getChildren().addAll(createSidePaneBack());

		sidePane.visibleProperty().bind(Clock.sidePaneVisible);

		Clock.sidePaneVisible.addListener((arg, oldVal, newVal) -> {
			if (newVal) {
				stage.setWidth(ClockStyle.RADIUS + ClockStyle.PANE_WIDTH);

			} else {
				stage.setWidth(ClockStyle.DIAM);
			}

			Clock.setPos(Clock.getPos());
		});

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