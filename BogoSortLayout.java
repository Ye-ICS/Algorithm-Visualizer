import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
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
import java.util.Collections;
import java.util.List;
import java.util.Random;

class BogoSortLayout extends BorderPane {
    private List<Rectangle> bars = new ArrayList<>();
    private List<Integer> values = new ArrayList<>();
    private Timeline timeline;
    private Pane barsContainer;
    private Slider speedSlider;
    private Slider barCountSlider;
    private TextField customValueField;
    private int selectedBarIndex = -1;
    private Rectangle selectedBarHighlight;
    private Label stepCounterLabel;
    private Label shuffleCounterLabel;
    private int stepCounter = 0;
    private int shuffleCounter = 0;
    private Button pauseResumeBtn;
    private boolean isPaused = false;
    private boolean isDragging = false;
    private double dragStartY;
    private double barStartHeight;
    private Random random = new Random();

    BogoSortLayout() {
        setPrefSize(800, 600); // Size for entire layout

        Text title = new Text("Bogo Sort");
        title.setFont(Font.font("System", FontWeight.BOLD, 24));
        BorderPane.setAlignment(title, Pos.CENTER);
        setTop(title);
        BorderPane.setMargin(title, new Insets(20, 0, 10, 0));

        barsContainer = new Pane();
        barsContainer.setPrefSize(750, 400);
        barsContainer.setStyle("-fx-background-color: #f0f0f0; -fx-border-color: #cccccc; -fx-border-width: 1px;");
        setCenter(barsContainer);
        BorderPane.setMargin(barsContainer, new Insets(10));
        
        // Create controls panel
        VBox controlsPanel = createControlsPanel();
        setRight(controlsPanel);
        BorderPane.setMargin(controlsPanel, new Insets(10));
        
        // Create button box
        HBox buttonBox = createButtonBox();
        setBottom(buttonBox);
        
        // Create selector highlight rectangle
        selectedBarHighlight = new Rectangle(0, 0, 0, 0);
        selectedBarHighlight.setFill(Color.TRANSPARENT);
        selectedBarHighlight.setStroke(Color.RED);
        selectedBarHighlight.setStrokeWidth(2);
        selectedBarHighlight.setVisible(false);
        selectedBarHighlight.setMouseTransparent(true); // Make outline non-interactive
        
        // Initialize bars with default count
        initializeBars((int)barCountSlider.getValue());
    }

    private VBox createControlsPanel() {
        VBox panel = new VBox(15);
        panel.setPadding(new Insets(10));
        panel.setStyle("-fx-background-color: #f8f8f8; -fx-border-color: #cccccc; -fx-border-width: 1px;");
        panel.setPrefWidth(200);
        
        // Speed control
        Label speedLabel = new Label("Sorting Speed:");
        speedSlider = new Slider(10, 1000, 100);
        speedSlider.setShowTickLabels(true);
        speedSlider.setShowTickMarks(true);
        
        // Bar count control
        Label barCountLabel = new Label("Number of Bars:");
        barCountSlider = new Slider(2, 20, 8); // Lower default for BogoSort since it's inefficient
        barCountSlider.setShowTickLabels(true);
        barCountSlider.setShowTickMarks(true);
        barCountSlider.setMajorTickUnit(5);
        barCountSlider.setBlockIncrement(1);
    
        Button applyBarCountBtn = new Button("Apply");
        applyBarCountBtn.setOnAction(event -> initializeBars((int)barCountSlider.getValue()));
        
        // Custom value input
        Label customValueLabel = new Label("Custom Bar Value:");
        customValueField = new TextField();
        customValueField.setPromptText("Enter value (10-400)");
        
        Button applyValueBtn = new Button("Apply to Selected");
        applyValueBtn.setOnAction(event -> applyCustomValue());
        
        // Step counters
        stepCounterLabel = new Label("Checks: 0");
        stepCounterLabel.setFont(Font.font("System", FontWeight.BOLD, 14));
        
        shuffleCounterLabel = new Label("Shuffles: 0");
        shuffleCounterLabel.setFont(Font.font("System", FontWeight.BOLD, 14));
        
        // Instructions
        Label instructionsLabel = new Label("Click on a bar to select it\nDrag the top of a bar to resize it");
        instructionsLabel.setWrapText(true);
        
        
        panel.getChildren().addAll(
            speedLabel, speedSlider,
            barCountLabel, barCountSlider, applyBarCountBtn,
            customValueLabel, customValueField, applyValueBtn,
            stepCounterLabel, shuffleCounterLabel,
            instructionsLabel
        );
        
        return panel;
    }
    
    private HBox createButtonBox() {
        HBox buttonBox = new HBox(20);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setPadding(new Insets(15));
        
        Button backBtn = new Button("Back");
        backBtn.setPrefSize(100, 30);
        backBtn.setOnAction(event -> FXUtils.setSceneRoot(getScene(), new MaximMenuLayout()));
        
        Button startBtn = new Button("Start Sorting");
        startBtn.setPrefSize(150, 30);
        startBtn.setOnAction(event -> startBogoSort());
        
        pauseResumeBtn = new Button("Pause");
        pauseResumeBtn.setPrefSize(100, 30);
        pauseResumeBtn.setDisable(true);
        pauseResumeBtn.setOnAction(event -> togglePauseResume());
        
        Button randomizeBtn = new Button("Randomize");
        randomizeBtn.setPrefSize(120, 30);
        randomizeBtn.setOnAction(event -> randomizeBars());
        
        buttonBox.getChildren().addAll(backBtn, startBtn, pauseResumeBtn, randomizeBtn);
        return buttonBox;
    }

    private void initializeBars(int count) {
        barsContainer.getChildren().clear();
        bars.clear();
        values.clear();
        
        double barWidth = (barsContainer.getPrefWidth() - 50) / count;
        double maxHeight = barsContainer.getPrefHeight() - 50;
        
        for (int i = 0; i < count; i++) {
            int value = random.nextInt((int)maxHeight) + 20; //Random heights
            
            Rectangle bar = new Rectangle(barWidth - 2, value);
            bar.setX(i * barWidth + 25); 
            bar.setY(barsContainer.getPrefHeight() - value - 20); 
            bar.setFill(Color.rgb(0, 102, 204, 0.8)); //inside
            bar.setStroke(Color.rgb(0, 70, 140)); //Border
            bar.setArcWidth(3);
            bar.setArcHeight(3);
            
            final int index = i;
            
            // Click handler for selection
            bar.setOnMouseClicked(event -> {
                if (!isDragging) { // Only select if not dragging
                    selectBar(index);
                }
            });
            
            // Add mouse pressed handler
            bar.setOnMousePressed(event -> {
                // Check if mouse is near the top of the bar (within 10 pixels)
                if (Math.abs(event.getY() - bar.getY()) <= 10 && 
                        (timeline == null || timeline.getStatus() != Timeline.Status.RUNNING || isPaused)) {
                    isDragging = true;
                    dragStartY = event.getY();
                    barStartHeight = bar.getHeight();
                    selectBar(index); // Also select the bar
                    event.consume();
                }
            });
            
            // Add mouse dragged handler
            bar.setOnMouseDragged(event -> {
                if (isDragging && selectedBarIndex == index) {
                    // Calculate new height based on drag distance
                    double deltaY = dragStartY - event.getY();
                    double newHeight = Math.max(10, Math.min(barStartHeight + deltaY, maxHeight));
                    
                    // Update bar height
                    bar.setHeight(newHeight);
                    bar.setY(barsContainer.getPrefHeight() - newHeight - 20);
                    
                    // Update value
                    values.set(index, (int)newHeight);
                    
                    // Update custom value field
                    customValueField.setText(Integer.toString((int)newHeight));
                    
                    // Update highlight rectangle
                    updateHighlightRectangle();
                    
                    event.consume();
                }
            });
            
            // Add mouse released handler
            bar.setOnMouseReleased(event -> {
                if (isDragging) {
                    isDragging = false;
                    event.consume();
                    
                    // Reset counters since we've modified the array
                    resetCounters();
                }
            });
            
            bars.add(bar);
            values.add(value);
            barsContainer.getChildren().add(bar);
        }
        
        // Add selection highlight rectangle to the container
        barsContainer.getChildren().add(0, selectedBarHighlight);
        
        // Add mouse tracking for the entire pane to handle cases where the user drags outside the bar
        barsContainer.setOnMouseDragged(event -> {
            if (isDragging && selectedBarIndex != -1) {
                Rectangle bar = bars.get(selectedBarIndex);
                
                // Calculate new height based on drag distance
                double deltaY = dragStartY - event.getY();
                double newHeight = Math.max(10, Math.min(barStartHeight + deltaY, maxHeight));
                
                // Update bar height
                bar.setHeight(newHeight);
                bar.setY(barsContainer.getPrefHeight() - newHeight - 20);
                
                // Update value
                values.set(selectedBarIndex, (int)newHeight);
                
                // Update custom value field
                customValueField.setText(Integer.toString((int)newHeight));
                
                // Update highlight rectangle
                updateHighlightRectangle();
            }
        });
        
        barsContainer.setOnMouseReleased(event -> {
            if (isDragging) {
                isDragging = false;
                
                // Reset counters since we've modified the array
                resetCounters();
            }
        });
        
        selectedBarIndex = -1;
        selectedBarHighlight.setVisible(false);
        
        // Reset counters
        resetCounters();
    }
    
    private void updateHighlightRectangle() {
        if (selectedBarIndex != -1) {
            Rectangle selectedBar = bars.get(selectedBarIndex);
            selectedBarHighlight.setX(selectedBar.getX() - 2);
            selectedBarHighlight.setY(selectedBar.getY() - 2);
            selectedBarHighlight.setWidth(selectedBar.getWidth() + 4);
            selectedBarHighlight.setHeight(selectedBar.getHeight() + 4);
        }
    }
    
    private void selectBar(int index) {
        if (timeline != null && timeline.getStatus() == Timeline.Status.RUNNING && !isPaused) {
            return; // Don't allow selection during active sorting
        }
        
        selectedBarIndex = index;
        updateHighlightRectangle();
        selectedBarHighlight.setVisible(true);
        
        // Update value field
        customValueField.setText(Integer.toString(values.get(index)));
    }
    
    private void applyCustomValue() {
        if (selectedBarIndex == -1) {
            // No bar selected
            return;
        }
        
        try {
            int value = Integer.parseInt(customValueField.getText());
            // Constrain value between 10 and max height
            value = Math.max(10, Math.min(value, (int)barsContainer.getPrefHeight() - 50));
            
            // Update value and bar height
            values.set(selectedBarIndex, value);
            Rectangle bar = bars.get(selectedBarIndex);
            bar.setHeight(value);
            bar.setY(barsContainer.getPrefHeight() - value - 20);
            
            // Update highlight rectangle
            updateHighlightRectangle();
            
        } catch (NumberFormatException e) {
            // Invalid input - ignore
        }
    }
    
    private void randomizeBars() {
        if (timeline != null && timeline.getStatus() == Timeline.Status.RUNNING && !isPaused) {
            return; // Don't allow randomization during active sorting
        }
        
        double maxHeight = barsContainer.getPrefHeight() - 50;
        
        for (int i = 0; i < bars.size(); i++) {
            int value = random.nextInt((int)maxHeight) + 20;
            values.set(i, value);
            
            Rectangle bar = bars.get(i);
            bar.setHeight(value);
            bar.setY(barsContainer.getPrefHeight() - value - 20);
        }
        
        // Update highlight if a bar is selected
        if (selectedBarIndex != -1) {
            updateHighlightRectangle();
            customValueField.setText(Integer.toString(values.get(selectedBarIndex)));
        }
        
        // Reset counters
        resetCounters();
    }
    
    private void resetCounters() {
        stepCounter = 0;
        shuffleCounter = 0;
        stepCounterLabel.setText("Checks: 0");
        shuffleCounterLabel.setText("Shuffles: 0");
    }
    
    private void incrementStepCounter() {
        stepCounter++;
        stepCounterLabel.setText("Checks: " + stepCounter);
    }
    
    private void incrementShuffleCounter() {
        shuffleCounter++;
        shuffleCounterLabel.setText("Shuffles: " + shuffleCounter);
    }

    private void startBogoSort() {
        if (timeline != null) {
            timeline.stop();
        }
         
        // Hide selection highlight during sorting
        selectedBarHighlight.setVisible(false);
        selectedBarIndex = -1;
        
        // Reset counters
        resetCounters();
        
        // Enable pause button
        pauseResumeBtn.setDisable(false);
        pauseResumeBtn.setText("Pause");
        isPaused = false;

        timeline = new Timeline();
        timeline.setCycleCount(Timeline.INDEFINITE);

        double speed = speedSlider.getValue();
        double duration = speed <= 500 ? 600 - speed : Math.max(1, 100 - (speed - 500) * 0.2);
        KeyFrame keyFrame = new KeyFrame(Duration.millis(duration), event -> bogoSortStep());
        
        timeline.getKeyFrames().add(keyFrame);
        timeline.play();
    }
    
    private void togglePauseResume() {
        if (timeline == null) {
            return;
        }
        
        if (isPaused) {
            // Resume
            timeline.play();
            pauseResumeBtn.setText("Pause");
            isPaused = false;
        } else {
            // Pause
            timeline.pause();
            pauseResumeBtn.setText("Resume");
            isPaused = true;
        }
    }

    private void bogoSortStep() {
        // Check if the array is sorted
        incrementStepCounter();
        boolean isSorted = true;
        
        // Highlight all bars in yellow to show we're checking
        for (Rectangle bar : bars) {
            highlightBar(bar, Color.YELLOW);
        }
        
        // Check if sorted in ascending order
        for (int i = 0; i < values.size() - 1; i++) {
            if (values.get(i) > values.get(i + 1)) {
                isSorted = false;
                // Highlight the out-of-order bars in red
                highlightBar(bars.get(i), Color.RED);
                highlightBar(bars.get(i + 1), Color.RED);
                break;
            }
        }

        if (isSorted) {
            // We've sorted the array! Stop and highlight in green
            timeline.stop();
            for (Rectangle bar : bars) {
                highlightBar(bar, Color.rgb(40, 180, 40));
            }
            
            // Disable pause button
            pauseResumeBtn.setDisable(true);
            pauseResumeBtn.setText("Pause");
            isPaused = false;
        } else {
            // Not sorted, shuffle and try again
            shuffleArray();
            incrementShuffleCounter();
        }
    }

    private void shuffleArray() {
        // Create a list of indices
        List<Integer> indices = new ArrayList<>();
        for (int i = 0; i < values.size(); i++) {
            indices.add(i);
        }
        
        // Shuffle the indices
        Collections.shuffle(indices, random);
        
        // Create temporary lists to hold shuffled values and bars
        List<Integer> newValues = new ArrayList<>(Collections.nCopies(values.size(), 0));
        List<Rectangle> newBars = new ArrayList<>(Collections.nCopies(bars.size(), null));
        
        // Rearrange values and bars based on shuffled indices
        for (int i = 0; i < indices.size(); i++) {
            int newPosition = indices.get(i);
            newValues.set(i, values.get(newPosition));
            newBars.set(i, bars.get(newPosition));
            
            // Update bar position
            Rectangle bar = bars.get(newPosition);
            double barWidth = bar.getWidth();
            bar.setX(i * (barWidth + 2) + 25);
            
            // Highlight the bar being moved
            highlightBar(bar, Color.PURPLE);
        }
        
        // Update lists
        values.clear();
        values.addAll(newValues);
        
        bars.clear();
        bars.addAll(newBars);
        
        // Update visual positions of bars
        updateBarsVisualPosition();
    }
    
    private void updateBarsVisualPosition() {
        double barWidth = (barsContainer.getPrefWidth() - 50) / bars.size();
        
        for (int i = 0; i < bars.size(); i++) {
            Rectangle bar = bars.get(i);
            bar.setX(i * barWidth + 25);
        }
    }
    
    private void highlightBar(Rectangle bar, Color color) {
        // Highlight bar temporarily
        Color originalColor = (Color) bar.getFill();
        bar.setFill(color);
        
        // Reset color after delay
        Timeline resetTimeline = new Timeline(
            new KeyFrame(Duration.millis(300), e -> bar.setFill(originalColor))
        );
        resetTimeline.play();
    }
}