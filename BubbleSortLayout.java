import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

class BubbleSortLayout extends BorderPane {
    private static final int MAX_BARS = 50;
    private List<Rectangle> bars = new ArrayList<>();
    private List<Integer> values = new ArrayList<>();
    private Timeline timeline;
    private Pane barsContainer;

    BubbleSortLayout() {

        setPrefSize(800, 600); // Size for entire layout

        Text title = new Text("Bubble Sort");
        title.setFont(Font.font("System", FontWeight.BOLD, 24)); //CHATGPT GIVEN, makes a cool font :)
        BorderPane.setAlignment(title, Pos.CENTER);
        setTop(title);
        BorderPane.setMargin(title, new Insets(20, 0, 10, 0));

        barsContainer = new Pane();
        barsContainer.setPrefSize(750, 400);
        barsContainer.setStyle("-fx-background-color: #f0f0f0; -fx-border-color: #cccccc; -fx-border-width: 1px;");
        setCenter(barsContainer);
        BorderPane.setMargin(barsContainer, new Insets(10));
        
        HBox buttonBox = new HBox(20);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setPadding(new Insets(15));
        
        Button backBtn = new Button("Back");
        backBtn.setPrefSize(100, 30);
        backBtn.setOnAction(event -> FXUtils.setSceneRoot(getScene(), new MenuLayout()));
        
        Button startBtn = new Button("Start Sorting");
        startBtn.setPrefSize(150, 30);
        startBtn.setOnAction(event -> startBubbleSort());
        
        Button resetBtn = new Button("Reset");
        resetBtn.setPrefSize(100, 30);
        resetBtn.setOnAction(event -> initializeBars());
        
        buttonBox.getChildren().addAll(backBtn, startBtn, resetBtn);
        setBottom(buttonBox);
        
        initializeBars();
    }

    private void initializeBars() {
        barsContainer.getChildren().clear();
        bars.clear();
        values.clear();
        
        Random random = new Random();
        double barWidth = (barsContainer.getPrefWidth() - 50) / MAX_BARS;
        double maxHeight = barsContainer.getPrefHeight() - 50;
        
        for (int i = 0; i < MAX_BARS; i++) {
            int value = random.nextInt((int)maxHeight) + 20; //Random heights
            
            Rectangle bar = new Rectangle(barWidth - 2, value);
            bar.setX(i * barWidth + 25); 
            bar.setY(barsContainer.getPrefHeight() - value - 20); 
            bar.setFill(Color.rgb(0, 102, 204, 0.8)); //inside
            bar.setStroke(Color.rgb(0, 70, 140)); //Border
            bar.setArcWidth(3);
            bar.setArcHeight(3);
            
            bars.add(bar);
            values.add(value);
            barsContainer.getChildren().add(bar);
        }
    }

    private void startBubbleSort() {
        if (timeline != null) {
            timeline.stop();
        }

        timeline = new Timeline();
        timeline.setCycleCount(Timeline.INDEFINITE);

        KeyFrame keyFrame = new KeyFrame(Duration.millis(100), event -> bubbleSortStep());
        timeline.getKeyFrames().add(keyFrame);
        timeline.play();
    }

    private void bubbleSortStep() {
        boolean swapped = false;
        for (int i = 0; i < values.size() - 1; i++) {
            if (values.get(i) > values.get(i + 1)) {
                swap(i, i + 1);
                swapped = true;
                
                highlightBar(bars.get(i), Color.GREEN);
                highlightBar(bars.get(i + 1), Color.GREEN);
                
                break; // Stops to make it go slower
            }
        }

        if (!swapped) {
            timeline.stop(); // if no swaps made, stop
            // Complted! change clr to green
            for (Rectangle bar : bars) {
                highlightBar(bar, Color.rgb(40, 180, 40));
            }
        }
    }

    private void swap(int i, int j) {
        // Swap values
        int tempValue = values.get(i);
        values.set(i, values.get(j));
        values.set(j, tempValue);

        // Swap the visual bars
        Rectangle bar1 = bars.get(i);
        Rectangle bar2 = bars.get(j);

        double tempHeight = bar1.getHeight();
        double tempY = bar1.getY();
        
        bar1.setHeight(bar2.getHeight());
        bar1.setY(bar2.getY());
        
        bar2.setHeight(tempHeight);
        bar2.setY(tempY);
    }
    
    private void highlightBar(Rectangle bar, Color color) {
        //Hightlight bar temporarily
        Color originalColor = (Color) bar.getFill();
        bar.setFill(color);
        
        //Reset colour after delay
        Timeline resetTimeline = new Timeline(
            new KeyFrame(Duration.millis(300), e -> bar.setFill(originalColor))
        );
        resetTimeline.play();
    }
}