import java.util.Scanner;
import javafx.scene.control.Button;
import javafx.scene.layout.FlowPane;
import javafx.scene.text.Text;

public class EditDistanceC extends FlowPane{
    EditDistanceC() {
        Text display = new Text("The result will show up here");
        Button submitBtn = new Button("Go");
        submitBtn.setOnAction(event -> display.setText("Distance: " + testEditDistance("hi", "he")));

        getChildren().addAll(display, submitBtn); //Basic structure for testing
    }


    int testEditDistance(String string, String otherString) {
        //Backend only for now for testing
        System.out.println("Welcome! Please input string number 1 you want to compare:");

        String[] compOne; 
        compOne = new String[string.length()];

        System.out.println("Please input string number 2 you want to compare:");

        String[] compTwo; 
        compTwo = new String[otherString.length()]; 


        //Fill each array index[i] with corresponding letter in index[i] from string input1
        for (int i = 0; i != string.length(); i++) {
            compOne[i] = string.substring(i);
        } 
        //Same thing but for second array - for future use in ease of comparing.
        for (int i = 0; i != otherString.length(); i++) {
            compTwo[i] = otherString.substring(i);
        } 
        System.out.println("Calculating...");

        return 0;

    }
}
