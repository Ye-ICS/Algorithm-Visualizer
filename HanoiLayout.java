import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Spinner;
import javafx.scene.layout.FlowPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;

public class HanoiLayout extends FlowPane {
    private Canvas canvas;
    private GraphicsContext gc;
    private Rectangle[] disks;

    private List<int[]> moves = new ArrayList<>();
    private int moveIndex = 0;

    private final int[] towerXPos = {100, 300, 500};
    
    private Text stepCounter;
    private Button nextStepBtn;

    HanoiLayout() {
        setAlignment(Pos.CENTER);

        Text description = new Text("Tower of Hanoi");

        stepCounter = new Text("Steps: 0"); // Initialize step counter

        canvas = new Canvas(600, 200);
        gc = canvas.getGraphicsContext2D();

        Spinner<Integer> levelSpinner = new Spinner<>(1, 8, 3, 1);
        levelSpinner.setEditable(true);

        Button backBtn = new Button("Back");
        backBtn.setOnAction(event -> FXUtils.setSceneRoot(getScene(), new MenuLayout()));

        Button setBtn = new Button("Set");
        setBtn.setOnAction(event -> {
            gc.clearRect(0, 0, 600, 200);
            int numDisks = levelSpinner.getValue();
            drawDisks(numDisks);
            generateMoves(numDisks, 0, 2, 1); // Generate move sequence
            moveIndex = 0;
            updateStepCounter(); // Reset step counter
        });

        nextStepBtn = new Button("Next Step"); // Store button reference
        nextStepBtn.setOnAction(event -> nextMove());

        getChildren().addAll(description, canvas, backBtn, setBtn, nextStepBtn, levelSpinner, stepCounter);
    }

    private void moveDiskAnimated(int diskIndex, double targetX, double targetY) {
        if (diskIndex < 0 || diskIndex >= disks.length) return;

        nextStepBtn.setDisable(true);

        Rectangle disk = disks[diskIndex];
        double startX = disk.getX();
        double startY = disk.getY();

        int frames = 60;
        double dx = (targetX - startX) / frames;
        double dy = (targetY - startY) / frames;

        Timeline timeline = new Timeline();

        for (int i = 0; i <= frames; i++) {
            int frame = i;
            timeline.getKeyFrames().add(new KeyFrame(Duration.millis(i * 10), e -> {
                disk.setX(startX + dx * frame);
                disk.setY(startY + dy * frame);
                redrawScene();
            }));
        }

        timeline.setOnFinished(event -> {
            disk.setX(targetX);
            disk.setY(targetY);
            redrawScene();
            updateStepCounter();
            nextStepBtn.setDisable(false);
        });

        timeline.play();
    }

    private void redrawScene() {
        gc.clearRect(0, 0, 600, 200);
        drawTowers();

        for (Rectangle disk : disks) {
            gc.setFill(disk.getFill());
            gc.fillRect(disk.getX(), disk.getY(), disk.getWidth(), disk.getHeight());
        }
    }

    private void drawTowers() {
        gc.setFill(Color.GRAY);
        gc.fillRect(50, 150, 100, 10);
        gc.fillRect(250, 150, 100, 10);
        gc.fillRect(450, 150, 100, 10);
        gc.fillRect(95, 50, 10, 100);
        gc.fillRect(295, 50, 10, 100);
        gc.fillRect(495, 50, 10, 100);
    }

    private void drawDisks(int numDisks) {
        gc.clearRect(0, 0, 600, 200);
        drawTowers();
    
        Color[] diskColors = new Color[]{
            Color.PURPLE, Color.BLUE, Color.CYAN, Color.GREEN, Color.LIGHTGREEN,
            Color.YELLOW, Color.ORANGE, Color.RED
        };
    
        disks = new Rectangle[numDisks];

        double baseYPosition = 150;
        double diskHeight = 10;
    
        for (int i = 0; i < numDisks; i++) {
            double diskWidth = 60 + (20 * (numDisks - i - 1));
            double xPosition = towerXPos[0] - diskWidth / 2; 
            double yPosition = baseYPosition - (i * diskHeight) - diskHeight;
            
            disks[i] = new Rectangle(xPosition, yPosition, diskWidth, diskHeight);
            disks[i].setFill(diskColors[i % diskColors.length]);
        }
        
        for (Rectangle disk : disks) {
            gc.setFill(disk.getFill());
            gc.fillRect(disk.getX(), disk.getY(), disk.getWidth(), disk.getHeight());
        }
    }

    private void generateMoves(int n, int from, int to, int aux) {
        moves.clear();
        recursionSolving(n, from, to, aux);
    }

    private void recursionSolving(int n, int from, int to, int aux) {
        if (n == 1) {
            moves.add(new int[]{from, to});
            return;
        }
        recursionSolving(n - 1, from, aux, to);
        moves.add(new int[]{from, to});
        recursionSolving(n - 1, aux, to, from);
    }

    private void nextMove() {
        if (moveIndex < moves.size()) {
            int[] move = moves.get(moveIndex);
            int from = move[0];
            int to = move[1];

            int diskIndex = findTopDisk(from);
            if (diskIndex != -1) {
                double x = towerXPos[to] - disks[diskIndex].getWidth() / 2;
                double y = calculateNewYPosition(to);

                moveDiskAnimated(diskIndex, x, y);
            }

            moveIndex++;
        }
    }

    private int findTopDisk(int towerNum) {
        double towerX = towerXPos[towerNum];
        int topDisk = -1;
        double topY = Double.MAX_VALUE;

        for (int i = 0; i < disks.length; i++) {
            if (Math.abs(disks[i].getX() + disks[i].getWidth() / 2 - towerX) < 10) {
                if (disks[i].getY() < topY) {
                    topDisk = i;
                    topY = disks[i].getY();
                }
            }
        }
        return topDisk;
    }

    private double calculateNewYPosition(int towerNum) {
        double baseY = 150;
        double diskHeight = 10;
        int count = 0;

        for (Rectangle disk : disks) {
            if (Math.abs(disk.getX() + disk.getWidth() / 2 - towerXPos[towerNum]) < 10) {
                count++;
            }
        }

        return baseY - (count * diskHeight) - diskHeight;
    }

    private void updateStepCounter() {
        stepCounter.setText("Steps: " + moveIndex);
    }
}
