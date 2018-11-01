package com.fxgraph.edges;

import com.fxgraph.graph.Graph;
import com.fxgraph.graph.ICell;

import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.Orientation;
import javafx.scene.Group;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;

public class DoubleCorneredEdge extends AbstractEdge {

	private final StringProperty textProperty;
	private final Orientation orientation;

	public DoubleCorneredEdge(ICell source, ICell target, Orientation orientation) {
		super(source, target);
		this.orientation = orientation;
		textProperty = new SimpleStringProperty();
	}

	@Override
	public EdgeGraphic getGraphic(Graph graph) {
		return new EdgeGraphic(graph, this, orientation, textProperty);
	}

	public StringProperty textProperty() {
		return textProperty;
	}

	public static class EdgeGraphic extends Pane {

		private final DoubleBinding sourceX;
		private final DoubleBinding sourceY;
		private final DoubleBinding targetX;
		private final DoubleBinding targetY;
		private final DoubleBinding centerX;
		private final DoubleBinding centerY;
		private final Group group;
		private final Line lineA;
		private final Line lineB;
		private final Line lineC;
		private final Text text;

		public EdgeGraphic(Graph graph, DoubleCorneredEdge edge, Orientation orientation, StringProperty textProperty) {
			sourceX = edge.getSource().getXAnchor(graph, edge);
			sourceY = edge.getSource().getYAnchor(graph, edge);
			targetX = edge.getTarget().getXAnchor(graph, edge);
			targetY = edge.getTarget().getYAnchor(graph, edge);

			centerX = sourceX.add(targetX).divide(2);
			centerY = sourceY.add(targetY).divide(2);

			text = new Text();
			text.textProperty().bind(textProperty);
			text.getStyleClass().add("edge-text");
			final DoubleProperty textWidth = new SimpleDoubleProperty();
			final DoubleProperty textHeight = new SimpleDoubleProperty();
			final Runnable recalculateWidth = () -> {
				textWidth.set(text.getLayoutBounds().getWidth());
				textHeight.set(text.getLayoutBounds().getHeight());
			};
			text.parentProperty().addListener((obs, oldVal, newVal) -> recalculateWidth.run());
			text.textProperty().addListener((obs, oldVal, newVal) -> recalculateWidth.run());

			if(orientation == Orientation.HORIZONTAL) {
				group = new Group();

				lineA = new Line();
				lineA.startXProperty().bind(sourceX);
				lineA.startYProperty().bind(sourceY);
				lineA.endXProperty().bind(centerX);
				lineA.endYProperty().bind(sourceY);
				group.getChildren().add(lineA);

				lineB = new Line();
				lineB.startXProperty().bind(centerX);
				lineB.startYProperty().bind(sourceY);
				lineB.endXProperty().bind(centerX);
				lineB.endYProperty().bind(targetY);
				group.getChildren().add(lineB);

				lineC = new Line();
				lineC.startXProperty().bind(centerX);
				lineC.startYProperty().bind(targetY);
				lineC.endXProperty().bind(targetX);
				lineC.endYProperty().bind(targetY);
				group.getChildren().add(lineC);

			} else {
				group = new Group();

				lineA = new Line();
				lineA.startXProperty().bind(sourceX);
				lineA.startYProperty().bind(sourceY);
				lineA.endXProperty().bind(sourceX);
				lineA.endYProperty().bind(centerY);
				group.getChildren().add(lineA);

				lineB = new Line();
				lineB.startXProperty().bind(sourceX);
				lineB.startYProperty().bind(centerY);
				lineB.endXProperty().bind(targetX);
				lineB.endYProperty().bind(centerY);
				group.getChildren().add(lineB);

				lineC = new Line();
				lineC.startXProperty().bind(targetX);
				lineC.startYProperty().bind(centerY);
				lineC.endXProperty().bind(targetX);
				lineC.endYProperty().bind(targetY);
				group.getChildren().add(lineC);
			}
			text.xProperty().bind(centerX.subtract(textWidth.divide(2)));
			text.yProperty().bind(centerY.subtract(textHeight.divide(2)));

			group.getChildren().add(text);
			getChildren().add(group);
		}

		public DoubleBinding getSourceX() {
			return sourceX;
		}

		public DoubleBinding getSourceY() {
			return sourceY;
		}

		public DoubleBinding getTargetX() {
			return targetX;
		}

		public DoubleBinding getTargetY() {
			return targetY;
		}

		public DoubleBinding getCenterX() {
			return centerX;
		}

		public DoubleBinding getCenterY() {
			return centerY;
		}

		public Group getGroup() {
			return group;
		}

		public Line getLineA() {
			return lineA;
		}

		public Line getLineB() {
			return lineB;
		}

		public Line getLineC() {
			return lineC;
		}

		public Text getText() {
			return text;
		}

	}

}