import javafx.animation.FadeTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
public class AESKeyExpansion extends VBox {  
    private final Color[] COLORS = {  
        Color.web("#3498db"),  // Blue for initial keys  
        Color.web("#e74c3c"),  // Red for operations  
        Color.web("#2ecc71"),  // Green for results  
        Color.web("#9b59b6"),  // Purple for intermediates  
        Color.web("#f39c12")   // Orange for highlights  
    };  
    
    private int currentStep = 0;  
    private VBox animationContainer;  
    private Label stepDescription;  
    private Button nextButton;  
    private Button prevButton;  
    private int[][] allExpandedKeys;  
    
    public AESKeyExpansion(String password, String plaintext) throws RuntimeException {  
        // Basic setup  
        setAlignment(Pos.CENTER);  
        setPrefSize(900, 700);  
        setSpacing(30);  
        setBackground(new Background(new BackgroundFill(  
            Color.web("#f4f4f9"), CornerRadii.EMPTY, Insets.EMPTY)));  
        setPadding(new Insets(20));  
        
        // Title and description  
        Label title = new Label("AES-256 Key Expansion");  
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");  
        
        // Generate the key and expanded keys  
        byte[] key = get256BitKey(password);  
        int[] intKey = byteArrayToIntArray(key);  
        allExpandedKeys = generateRoundKeys(intKey);  
        
        // Create the step description  
        stepDescription = new Label("Initial Keys (Round Keys 0-1)");  
        stepDescription.setStyle(  
            "-fx-font-size: 18px; -fx-font-weight: bold; " +  
            "-fx-background-color: #2c3e50; -fx-text-fill: white; " +  
            "-fx-padding: 10px 20px; -fx-background-radius: 5px;"  
        );  
        
        // Create the animation container with scroll capability  
        animationContainer = new VBox(20);  
        animationContainer.setAlignment(Pos.CENTER);  
        
        ScrollPane scrollPane = new ScrollPane(animationContainer);  
        scrollPane.setFitToWidth(true);  
        scrollPane.setPrefHeight(450);  
        scrollPane.setStyle("-fx-background: transparent; -fx-background-color: transparent;");  
        
        // Create navigation buttons  
        HBox buttonBar = createNavigationButtons();  
        
        // Initialize with first view (initial keys)  
        displayInitialKeys(key);  
        
        // Add everything to the main container  
        getChildren().addAll(title, stepDescription, scrollPane, buttonBar);  
        
        // Add back button  
        Button backButton = new Button("Back");  
        backButton.setStyle(  
            "-fx-background-color: #3498db; -fx-text-fill: white; " +  
            "-fx-font-size: 14px; -fx-padding: 8px 15px;"  
        );  
        backButton.setOnAction(_ -> FXUtils.setSceneRoot(getScene(),   
            new AESPasswordArray(AEStart.getPassword(), AEStart.getPlaintext())));  
        getChildren().add(backButton);  
        
        // Initial fade-in  
        FadeTransition fadeIn = new FadeTransition(Duration.seconds(1), this);  
        fadeIn.setFromValue(0);  
        fadeIn.setToValue(1);  
        fadeIn.play();  
    }  
    
    @SuppressWarnings("unused")
    private HBox createNavigationButtons() {  
        HBox buttonBar = new HBox(20);  
        buttonBar.setAlignment(Pos.CENTER);  
        
        prevButton = new Button("Previous Step");  
        prevButton.setStyle(  
            "-fx-background-color: #34495e; -fx-text-fill: white; " +  
            "-fx-font-size: 14px; -fx-padding: 8px 15px;"  
        );  
        prevButton.setDisable(true);  
        prevButton.setOnAction(e -> showPreviousStep());  
        
        nextButton = new Button("Next Step");  
        nextButton.setStyle(  
            "-fx-background-color: #2ecc71; -fx-text-fill: white; " +  
            "-fx-font-size: 14px; -fx-padding: 8px 15px;"  
        );  
        nextButton.setOnAction(e -> showNextStep());  
        
        buttonBar.getChildren().addAll(prevButton, nextButton);  
        return buttonBar;  
    }  
    
    private int[] byteArrayToIntArray(byte[] bytes) {  
        int[] result = new int[bytes.length];  
        for (int i = 0; i < bytes.length; i++) {  
            result[i] = bytes[i] & 0xFF;  
        }  
        return result;  
    }  
    
    private void displayInitialKeys(byte[] key) {  
        animationContainer.getChildren().clear();  
        
        // Show the original key in hex  
        Text originalKeyText = new Text("Original 256-bit Key (Hex): " + bytesToHex(key));  
        originalKeyText.setStyle("-fx-font-size: 14px; -fx-fill: #2c3e50;");  
        
        // Create key matrices to display  
        int[][][] keyMatrices = splitKeyIntoMatrices(key);  
        
        // Create heading  
        Text heading = new Text("The 256-bit key is split into two 128-bit round keys:");  
        heading.setStyle("-fx-font-size: 16px; -fx-fill: #2c3e50;");  
        
        // Create container for the two keys  
        HBox keysContainer = new HBox(30);  
        keysContainer.setAlignment(Pos.CENTER);  
        
        // Create visual displays for the two keys  
        VBox key0Display = createKeyMatrixDisplay(keyMatrices[0], "Round Key 0", COLORS[0]);  
        VBox key1Display = createKeyMatrixDisplay(keyMatrices[1], "Round Key 1", COLORS[0]);  
        
        keysContainer.getChildren().addAll(key0Display, key1Display);  
        
        // Add all to container with animations  
        animationContainer.getChildren().addAll(originalKeyText, heading, keysContainer);  
        
        // Explanation of what comes next  
        Text nextStepsText = new Text(  
            "In the next steps, we'll see how the remaining 13 round keys (2-14) are generated " +  
            "using the key expansion algorithm."  
        );  
        nextStepsText.setStyle("-fx-font-size: 16px; -fx-fill: #2c3e50; -fx-font-style: italic;");  
        animationContainer.getChildren().add(nextStepsText);  
        
        // Add fade-in animation to each element  
        for (Node node : animationContainer.getChildren()) {  
            FadeTransition ft = new FadeTransition(Duration.millis(800), node);  
            ft.setFromValue(0);  
            ft.setToValue(1);  
            ft.play();  
        }  
    }  
    
    private VBox createKeyMatrixDisplay(int[][] matrix, String title, Color color) {  
        VBox container = new VBox(10);  
        container.setAlignment(Pos.CENTER);  
        
        Label titleLabel = new Label(title);  
        titleLabel.setStyle(  
            "-fx-font-size: 16px; -fx-font-weight: bold; " +  
            "-fx-text-fill: " + colorToHex(color) + ";"  
        );  
        
        GridPane grid = new GridPane();  
        grid.setAlignment(Pos.CENTER);  
        grid.setHgap(5);  
        grid.setVgap(5);  
        grid.setPadding(new Insets(10));  
        grid.setStyle(  
            "-fx-background-color: white; " +  
            "-fx-border-color: " + colorToHex(color) + "; " +  
            "-fx-border-width: 2px; " +  
            "-fx-border-radius: 5px;"  
        );  
        
        // Create cells for the 4x4 matrix  
        for (int row = 0; row < 4; row++) {  
            for (int col = 0; col < 4; col++) {  
                Label cell = new Label(String.format("%02X", matrix[row][col]));  
                cell.setMinSize(40, 40);  
                cell.setAlignment(Pos.CENTER);  
                cell.setStyle(  
                    "-fx-background-color: " + colorToHex(color.deriveColor(0, 1, 1, 0.2)) + "; " +  
                    "-fx-border-color: " + colorToHex(color) + "; " +  
                    "-fx-border-width: 1px; " +  
                    "-fx-font-family: 'Courier New'; " +  
                    "-fx-font-weight: bold; " +  
                    "-fx-font-size: 14px;"  
                );  
                grid.add(cell, col, row);  
            }  
        }  
        
        container.getChildren().addAll(titleLabel, grid);  
        return container;  
    }  
    
    private String colorToHex(Color color) {  
        return String.format("#%02X%02X%02X",  
            (int)(color.getRed() * 255),  
            (int)(color.getGreen() * 255),  
            (int)(color.getBlue() * 255));  
    }  
    
    private void showNextStep() {  
        currentStep++;  
        prevButton.setDisable(false);  
        
        if (currentStep == 1) {  
            // Show an overview of the key expansion process  
            showKeyExpansionOverview();  
        } else {  
            // Show the generation of round keys 2-14  
            int roundKeyIndex = currentStep;  
            if (roundKeyIndex <= 14) {  
                showRoundKeyGeneration(roundKeyIndex);  
            } else {  
                // Final summary step  
                showKeySummary();  
                nextButton.setDisable(true);  
            }  
        }  
    }  
    
    private void showPreviousStep() {  
        currentStep--;  
        nextButton.setDisable(false);  
        
        if (currentStep == 0) {  
            // Back to initial keys  
            byte[] key = get256BitKey(AEStart.getPassword());  
            displayInitialKeys(key);  
            prevButton.setDisable(true);  
            stepDescription.setText("Initial Keys (Round Keys 0-1)");  
        } else if (currentStep == 1) {  
            // Show key expansion overview  
            showKeyExpansionOverview();  
        } else {  
            // Show the generation of the previous round key  
            showRoundKeyGeneration(currentStep);  
        }  
    }  
    
    private void showKeyExpansionOverview() {  
        animationContainer.getChildren().clear();  
        stepDescription.setText("AES Key Expansion Process");  
        
        VBox container = new VBox(20);  
        container.setAlignment(Pos.CENTER);  
        
        Text overview = new Text(  
            "AES-256 key expansion creates 15 round keys (labeled 0-14) from the original 256-bit key.\n" +  
            "• Round Keys 0-1: Directly from the original key\n" +  
            "• Round Keys 2-14: Generated through expansion operations\n\n" +  
            "The key expansion algorithm uses the following operations:"  
        );  
        overview.setStyle("-fx-font-size: 16px; -fx-fill: #2c3e50;");  
        
        VBox operationsBox = new VBox(10);  
        operationsBox.setAlignment(Pos.CENTER_LEFT);  
        operationsBox.setStyle(  
            "-fx-background-color: white; " +  
            "-fx-border-color: " + colorToHex(COLORS[1]) + "; " +  
            "-fx-border-width: 2px; " +  
            "-fx-border-radius: 5px; " +  
            "-fx-padding: 15px;"  
        );  
        
        Text rotWordText = new Text("• RotWord: Rotates a 4-byte word left by one byte");  
        rotWordText.setStyle("-fx-font-size: 14px; -fx-fill: #2c3e50;");  
        
        Text subWordText = new Text("• SubWord: Substitutes each byte using the AES S-Box");  
        subWordText.setStyle("-fx-font-size: 14px; -fx-fill: #2c3e50;");  
        
        Text rconText = new Text("• Rcon: XORs with a round constant (different for each round)");  
        rconText.setStyle("-fx-font-size: 14px; -fx-fill: #2c3e50;");  
        
        Text xorText = new Text("• XOR: Bitwise exclusive OR between words");  
        xorText.setStyle("-fx-font-size: 14px; -fx-fill: #2c3e50;");  
        
        operationsBox.getChildren().addAll(rotWordText, subWordText, rconText, xorText);  
        
        // Visual diagram of the expansion process  
        ImageView expansionDiagram = createKeyExpansionDiagram();  
        
        Text nextStepPrompt = new Text(  
            "In the next steps, we'll see how each round key is generated in detail."  
        );  
        nextStepPrompt.setStyle("-fx-font-size: 16px; -fx-fill: #2c3e50; -fx-font-style: italic;");  
        
        container.getChildren().addAll(overview, operationsBox, expansionDiagram, nextStepPrompt);  
        animationContainer.getChildren().add(container);  
        
        // Animation  
        FadeTransition ft = new FadeTransition(Duration.millis(800), container);  
        ft.setFromValue(0);  
        ft.setToValue(1);  
        ft.play();  
    }  
    
    private ImageView createKeyExpansionDiagram() {
        // Placeholder diagram for the key expansion process
        VBox diagramBox = new VBox(15);
        diagramBox.setAlignment(Pos.CENTER);
        diagramBox.setPadding(new Insets(10));
        diagramBox.setStyle(
            "-fx-background-color: white; " +
            "-fx-border-color: #95a5a6; " +
            "-fx-border-width: 1px; " +
            "-fx-border-radius: 5px;"
        );

        Label diagramTitle = new Label("Key Expansion Process");
        diagramTitle.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        // Add a placeholder for the diagram
        Label placeholder = new Label("Diagram Placeholder");
        placeholder.setStyle("-fx-font-size: 14px; -fx-text-fill: #2c3e50;");

        diagramBox.getChildren().addAll(diagramTitle, placeholder);

        // Return the VBox as a Node
        return new ImageView(); // Replace with actual diagram if available
    }
    
    private void showRoundKeyGeneration(int roundKeyIndex) {  
        animationContainer.getChildren().clear();  
        stepDescription.setText("Generating Round Key " + roundKeyIndex);  
        
        VBox container = new VBox(25);  
        container.setAlignment(Pos.CENTER);  
        
        Text title = new Text("Round Key " + roundKeyIndex + " Generation");  
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-fill: #2c3e50;");  
        
        // Convert word-based keys to matrix form for visualization  
        int[][] currentKeyMatrix = convertToMatrix(allExpandedKeys, roundKeyIndex);  
        int[][] prevKeyMatrix = convertToMatrix(allExpandedKeys, roundKeyIndex - 1);          
        // Description of the process based on key index  
        Text processDescription;  
        if (roundKeyIndex % 2 == 0) {  
            processDescription = new Text(  
                "For even-numbered keys, the algorithm applies:\n" +  
                "1. RotWord: Rotate the last column of previous key\n" +  
                "2. SubWord: Apply S-Box substitution to each byte\n" +  
                "3. XOR with round constant (Rcon[" + (roundKeyIndex/2) + "])\n" +  
                "4. XOR with corresponding column from two keys back"  
            );  
        } else {  
            processDescription = new Text(  
                "For odd-numbered keys, the algorithm applies:\n" +  
                "1. Direct XOR operations between previous key and\n" +  
                "   the corresponding column from two keys back\n" +  
                "2. No rotation, substitution, or round constant is used"  
            );  
        }  
        processDescription.setStyle("-fx-font-size: 16px; -fx-fill: #2c3e50;");  
        
        // Create animation panels  
        HBox keyGenerationVisual = new HBox(20);  
        keyGenerationVisual.setAlignment(Pos.CENTER);  
        
        // Panel showing previous key  
        VBox prevKeyPanel = createKeyMatrixDisplay(prevKeyMatrix,   
                                                 "Round Key " + (roundKeyIndex-1),   
                                                 COLORS[0]);  
        
        // Panel showing operations  
        VBox operationsPanel = createOperationsPanel(roundKeyIndex);  
        
        // Panel showing the current key (result)  
        VBox currentKeyPanel = createKeyMatrixDisplay(currentKeyMatrix,   
                                                    "Round Key " + roundKeyIndex,   
                                                    COLORS[2]);  
        
        keyGenerationVisual.getChildren().addAll(prevKeyPanel, operationsPanel, currentKeyPanel);  
        
        // Add detailed explanation of the calculations  
        Text detailedExplanation = createDetailedExplanation(roundKeyIndex);  
        
        container.getChildren().addAll(  
            title,   
            processDescription,   
            keyGenerationVisual,   
            detailedExplanation  
        );  
        
        animationContainer.getChildren().add(container);  
        
        // Animation  
        FadeTransition ft = new FadeTransition(Duration.millis(800), container);  
        ft.setFromValue(0);  
        ft.setToValue(1);  
        ft.play();  
    }  
    
    private int[][] convertToMatrix(int[][] expandedKeys, int roundKeyIndex) {  
        int[][] matrix = new int[4][4];  
        int startWordIndex = roundKeyIndex * 4;  
        
        // Handle indices that might be out of bounds for early rounds  
        if (startWordIndex < 0) startWordIndex = 0;  
        if (startWordIndex >= expandedKeys.length - 3) startWordIndex = expandedKeys.length - 4;  
        
        for (int col = 0; col < 4; col++) {  
            for (int row = 0; row < 4; row++) {  
                matrix[row][col] = expandedKeys[startWordIndex + col][row];  
            }  
        }  
        
        return matrix;  
    }  
    
    private VBox createOperationsPanel(int roundKeyIndex) {  
        VBox operationsPanel = new VBox(15);  
        operationsPanel.setAlignment(Pos.CENTER);  
        operationsPanel.setPadding(new Insets(10));  
        operationsPanel.setStyle(  
            "-fx-background-color: white; " +  
            "-fx-border-color: " + colorToHex(COLORS[1]) + "; " +  
            "-fx-border-width: 2px; " +  
            "-fx-border-radius: 5px;"  
        );  
        
        Label operationsTitle = new Label("Key Expansion Operations");  
        operationsTitle.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");  
        
        VBox stepsContainer = new VBox(10);  
        stepsContainer.setAlignment(Pos.CENTER);  
        
        // For even-numbered round keys, show all operations  
        // For odd-numbered, just show XOR  
        if (roundKeyIndex % 2 == 0) {              
            // Animation for RotWord  
            HBox rotWordBox = createOperationAnimation("RotWord",   
                new int[]{0x12, 0x34, 0x56, 0x78}, // Example values  
                rotWord(new int[]{0x12, 0x34, 0x56, 0x78}), // Example rotated  
                "Rotates the word left by one byte");  
            
            // Animation for SubWord  
            HBox subWordBox = createOperationAnimation("SubWord",  
                rotWord(new int[]{0x12, 0x34, 0x56, 0x78}), // From previous step  
                subWord(rotWord(new int[]{0x12, 0x34, 0x56, 0x78})), // Example substituted  
                "Substitutes each byte using the S-Box");  
            
            // Animation for Rcon XOR  
            HBox rconBox = createOperationAnimation("XOR with Rcon[" + (roundKeyIndex/2) + "]",  
                subWord(rotWord(new int[]{0x12, 0x34, 0x56, 0x78})), // From previous step  
                xorWords(subWord(rotWord(new int[]{0x12, 0x34, 0x56, 0x78})), rcon(roundKeyIndex/2)), // With Rcon  
                "XOR with round constant " + bytesToHex(intArrayToByteArray(rcon(roundKeyIndex/2))));  
            
            stepsContainer.getChildren().addAll(rotWordBox, subWordBox, rconBox);  
        }  
        
        // XOR with earlier key (common for all rounds)  
        Text xorExplanation = new Text("Finally, XOR with words from two keys back");  
        xorExplanation.setStyle("-fx-font-size: 14px; -fx-fill: #2c3e50;");  
        stepsContainer.getChildren().add(xorExplanation);  
        
        operationsPanel.getChildren().addAll(operationsTitle, stepsContainer);  
        return operationsPanel;  
    }  
    
    private HBox createOperationAnimation(String operationName, int[] input, int[] output, String description) {  
        HBox container = new HBox(15);  
        container.setAlignment(Pos.CENTER);  
        
        // Input word visualization  
        VBox inputBox = createWordDisplay(input, "Input", COLORS[0]);  
        
        // Arrow with operation name  
        VBox arrowBox = new VBox(5);  
        arrowBox.setAlignment(Pos.CENTER);  
        
        Text operationText = new Text(operationName);  
        operationText.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-fill: " + colorToHex(COLORS[1]) + ";");  
        
        Text arrowText = new Text("→");  
        arrowText.setStyle("-fx-font-size: 24px; -fx-fill: " + colorToHex(COLORS[1]) + ";");  
        
        Text descriptionText = new Text(description);  
        descriptionText.setStyle("-fx-font-size: 12px; -fx-fill: #2c3e50;");  
        
        arrowBox.getChildren().addAll(operationText, arrowText, descriptionText);  
        
        // Output word visualization  
        VBox outputBox = createWordDisplay(output, "Output", COLORS[2]);  
        
        container.getChildren().addAll(inputBox, arrowBox, outputBox);  
        
        // Add animation to highlight the transformation  
        FadeTransition highlight = new FadeTransition(Duration.millis(500), outputBox);  
        highlight.setFromValue(0.3);  
        highlight.setToValue(1.0);  
        highlight.setCycleCount(2);  
        highlight.setAutoReverse(true);  
        highlight.play();  
        
        return container;  
    }  
    
    private VBox createWordDisplay(int[] word, String label, Color color) {  
        VBox container = new VBox(5);  
        container.setAlignment(Pos.CENTER);  
        
        Label titleLabel = new Label(label);  
        titleLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: " + colorToHex(color) + ";");  
        
        HBox bytesBox = new HBox(2);  
        bytesBox.setAlignment(Pos.CENTER);  
        
        for (int i = 0; i < word.length; i++) {  
            Label byteLabel = new Label(String.format("%02X", word[i]));  
            byteLabel.setMinSize(30, 30);  
            byteLabel.setAlignment(Pos.CENTER);  
            byteLabel.setStyle(  
                "-fx-background-color: " + colorToHex(color.deriveColor(0, 1, 1, 0.2)) + "; " +  
                "-fx-border-color: " + colorToHex(color) + "; " +  
                "-fx-border-width: 1px; " +  
                "-fx-font-family: 'Courier New'; " +  
                "-fx-font-weight: bold; " +  
                "-fx-font-size: 12px;"  
            );  
            bytesBox.getChildren().add(byteLabel);  
        }  
        
        container.getChildren().addAll(titleLabel, bytesBox);  
        return container;  
    }  
    
    private Text createDetailedExplanation(int roundKeyIndex) {  
        StringBuilder sb = new StringBuilder();  
        
        sb.append("Detailed Calculation for Round Key ").append(roundKeyIndex).append(":\n\n");  
        
        if (roundKeyIndex % 2 == 0) {  
            sb.append("1. Take the last column from the previous round key\n");  
            sb.append("2. Apply RotWord to rotate the bytes: [a,b,c,d] → [b,c,d,a]\n");  
            sb.append("3. Apply SubWord to substitute each byte using the S-Box\n");  
            sb.append("4. XOR with the round constant: Rcon[").append(roundKeyIndex/2).append("]\n");  
            sb.append("5. XOR with the corresponding column from two keys back\n");  
            sb.append("6. Continue this pattern for remaining columns\n");  
        } else {  
            sb.append("1. Each column is calculated by XORing:\n");  
            sb.append("   • The corresponding column from the previous round key\n");  
            sb.append("   • The corresponding column from two keys back\n");  
            sb.append("2. No RotWord, SubWord, or Rcon operations are applied\n");  
        }  
        
        Text explanation = new Text(sb.toString());  
        explanation.setStyle("-fx-font-size: 14px; -fx-fill: #2c3e50;");  
        return explanation;  
    }  
    
    private void showKeySummary() {  
        animationContainer.getChildren().clear();  
        stepDescription.setText("AES-256 Key Expansion Complete");  
        
        VBox summaryContainer = new VBox(25);  
        summaryContainer.setAlignment(Pos.CENTER);  
        
        Text summaryTitle = new Text("All 15 Round Keys Generated");  
        summaryTitle.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-fill: #2c3e50;");  
        
        Text summaryText = new Text(  
            "The AES-256 key expansion process has generated all 15 round keys (0-14):\n" +  
            "• Round Keys 0-1: Derived directly from the original 256-bit key\n" +  
            "• Round Keys 2-14: Generated through the key expansion algorithm\n\n" +  
            "These round keys will be used in the different rounds of AES encryption and decryption."  
        );  
        summaryText.setStyle("-fx-font-size: 16px; -fx-fill: #2c3e50;");  
        
        // Create a visual grid of all the round keys  
        GridPane keysGrid = new GridPane();  
        keysGrid.setAlignment(Pos.CENTER);  
        keysGrid.setHgap(15);  
        keysGrid.setVgap(15);  
        
        // Display the keys in a 5×3 grid  
        for (int i = 0; i < 15; i++) {  
            int row = i / 5;  
            int col = i % 5;  
            
            int[][] keyMatrix = convertToMatrix(allExpandedKeys, i);  
            VBox miniKeyDisplay = createMiniKeyDisplay(keyMatrix, i);  
            
            keysGrid.add(miniKeyDisplay, col, row);  
        }  
        
        Text conclusionText = new Text(  
            "These keys are crucial to the security of AES encryption. Each round key is used\n" +  
            "in a specific round of the encryption process to add diffusion and confusion,\n" +  
            "making the algorithm resistant to cryptanalysis."  
        );  
        conclusionText.setStyle("-fx-font-size: 16px; -fx-fill: #2c3e50;");  
        
        summaryContainer.getChildren().addAll(  
            summaryTitle,  
            summaryText,  
            keysGrid,  
            conclusionText  
        );  
        
        animationContainer.getChildren().add(summaryContainer);  
        
        // Animation  
        FadeTransition ft = new FadeTransition(Duration.millis(1000), summaryContainer);  
        ft.setFromValue(0);  
        ft.setToValue(1);  
        ft.play();  
    }  
    
    private VBox createMiniKeyDisplay(int[][] matrix, int keyIndex) {  
        VBox container = new VBox(5);  
        container.setAlignment(Pos.CENTER);  
        
        // Choose a color based on the key index  
        Color keyColor = COLORS[keyIndex % COLORS.length];  
        
        Label keyLabel = new Label("Key " + keyIndex);  
        keyLabel.setStyle("-fx-font-size: 12px; -fx-font-weight: bold; -fx-text-fill: " + colorToHex(keyColor) + ";");  
        
        GridPane grid = new GridPane();  
        grid.setAlignment(Pos.CENTER);  
        grid.setHgap(2);  
        grid.setVgap(2);  
        grid.setPadding(new Insets(3));  
        grid.setStyle(  
            "-fx-background-color: white; " +  
            "-fx-border-color: " + colorToHex(keyColor) + "; " +  
            "-fx-border-width: 1px; " +  
            "-fx-border-radius: 3px;"  
        );  
        
        // Create small cells for the mini display  
        for (int row = 0; row < 4; row++) {  
            for (int col = 0; col < 4; col++) {  
                Label cell = new Label(String.format("%02X", matrix[row][col]));  
                cell.setMinSize(25, 25);  
                cell.setAlignment(Pos.CENTER);  
                cell.setStyle(  
                    "-fx-background-color: " + colorToHex(keyColor.deriveColor(0, 1, 1, 0.1)) + "; " +  
                    "-fx-font-family: 'Courier New'; " +  
                    "-fx-font-size: 10px;"  
                );  
                grid.add(cell, col, row);  
            }  
        }  
        
        container.getChildren().addAll(keyLabel, grid);  
        return container;  
    }



        // Method to generate a 256-bit key from a password using SHA-256 hashing
    public static byte[] get256BitKey(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            return digest.digest(password.getBytes(StandardCharsets.UTF_8)); // Returns 32 bytes
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 Algorithm Not Found");
        }
    }

    // Method to convert byte array to hex string (optional, if you want to display the raw key)
    public static String bytesToHex(byte[] bytes) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : bytes) {
            hexString.append(String.format("%02x", b));
        }
        return hexString.toString();
    }

    // Method to split the 256-bit key into two 4x4 matrices
    public static int[][][] splitKeyIntoMatrices(byte[] key) {
        int[][] key0 = new int[4][4];
        int[][] key1 = new int[4][4];

        // Fill the first 16 bytes into Key 0
        for (int i = 0; i < 16; i++) {
            key0[i % 4][i / 4] = key[i] & 0xFF;
        }

        // Fill the next 16 bytes into Key 1
        for (int i = 16; i < 32; i++) {
            key1[i % 4][(i - 16) / 4] = key[i] & 0xFF;
        }

        return new int[][][] { key0, key1 };
    }

        // AES S-Box for SubWord
    private static final int[] SBOX = {
        0x63, 0x7c, 0x77, 0x7b, 0xf2, 0x6b, 0x6f, 0xc5, 0x30, 0x01, 0x67, 0x2b, 0xfe, 0xd7, 0xab, 0x76,
        0xca, 0x82, 0xc9, 0x7d, 0xfa, 0x59, 0x47, 0xf0, 0xad, 0xd4, 0xa2, 0xaf, 0x9c, 0xa4, 0x72, 0xc0,
        0xb7, 0xfd, 0x93, 0x26, 0x36, 0x3f, 0xf7, 0xcc, 0x34, 0xa5, 0xe5, 0xf1, 0x71, 0xd8, 0x31, 0x15,
        0x04, 0xc7, 0x23, 0xc3, 0x18, 0x96, 0x05, 0x9a, 0x07, 0x12, 0x80, 0xe2, 0xeb, 0x27, 0xb2, 0x75,
    };

    // Rcon table
    private static final int[] RCON = {
        0x00, 0x01, 0x02, 0x04, 0x08, 0x10, 0x20, 0x40, 0x80, 0x1B, 0x36
    };


    // RotWord - Rotate 4-byte word left by 1 byte
    public static int[] rotWord(int[] word) {
        return new int[]{word[1], word[2], word[3], word[0]};
    }

    // SubWord - Apply AES S-Box substitution
    public static int[] subWord(int[] word) {
        int[] newWord = new int[4];
        for (int i = 0; i < 4; i++) {
            newWord[i] = SBOX[(word[i] & 0xFF) % SBOX.length];
        }
        return newWord;
    }

    // Rcon function - Generate round constants
    public static int[] rcon(int round) {
        return new int[]{RCON[round], 0x00, 0x00, 0x00};
    }

    // XOR function
    public static int[] xorWords(int[] word1, int[] word2) {
        int[] result = new int[4];
        for (int i = 0; i < 4; i++) {
            result[i] = word1[i] ^ word2[i];
        }
        return result;
    }

    // Method to convert int array to byte array
    public static byte[] intArrayToByteArray(int[] intArray) {
        byte[] byteArray = new byte[intArray.length];
        for (int i = 0; i < intArray.length; i++) {
            byteArray[i] = (byte) intArray[i];
        }
        return byteArray;
    }

    // Key Expansion function - Generates 15 round keys
    public static int[][] generateRoundKeys(int[] key) {
        int[][] expandedKeys = new int[60][4]; // 15 round keys (each 128 bits = 4 words)
    
        // First 8 words are copied directly from the original key
        for (int i = 0; i < 8; i++) {
            expandedKeys[i] = Arrays.copyOfRange(key, i * 4, (i + 1) * 4);
        }
    
        // Generate remaining words
        for (int i = 8; i < 60; i++) {
            int[] temp = expandedKeys[i - 1];
    
            if (i % 8 == 0) {
                // Apply RotWord, SubWord, and Rcon for every 8th word
                temp = xorWords(subWord(rotWord(temp)), rcon(i / 8)); // Fix Rcon access
            }
    
            // XOR with the word 8 positions before
            expandedKeys[i] = xorWords(expandedKeys[i - 8], temp);
        }
    
        return expandedKeys;
    }
    
    // Print the round keys (Formatted)
    public static void printRoundKeys(int[][] keys) {
        for (int i = 2; i < 15; i++) {
            System.out.printf("Round Key %d:\n", i);
            for (int j = 0; j < 4; j++) {
                System.out.printf("%02X %02X %02X %02X\n", keys[i * 4 + j][0], keys[i * 4 + j][1], keys[i * 4 + j][2], keys[i * 4 + j][3]);
            }
            System.out.println();
        }
    }
    public static void generate14RoundKeys(String password) throws NoSuchAlgorithmException {
    // Generate and split the key
    byte[] aesKey = get256BitKey(password);
    int[][][] keyMatrices = splitKeyIntoMatrices(aesKey);


    
    // Print the first two round keys (keyMatrices)
    System.out.println(bytesToHex(aesKey));
    System.out.println("Round Key 0:");
    printMatrix(keyMatrices[0]);
    System.out.println();
    System.out.println("Round Key 1:");
    printMatrix(keyMatrices[1]);
    System.out.println();

    // Expand the key
    int[] intKey = new int[aesKey.length];
    for (int i = 0; i < aesKey.length; i++) {
        intKey[i] = aesKey[i] & 0xFF;
    }
    int[][] roundKeys = generateRoundKeys(intKey);

    // Print the remaining round keys
    printRoundKeys(roundKeys);
}

// Helper method to print a 4x4 matrix
private static void printMatrix(int[][] matrix) {
    for (int i = 0; i < 4; i++) {
        for (int j = 0; j < 4; j++) {
            System.out.printf("%02X ", matrix[i][j]);
        }
        System.out.println();
    }
}
}