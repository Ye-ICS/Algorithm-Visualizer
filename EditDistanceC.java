import java.util.Scanner;

public class EditDistanceC {
    void EditDistancec() {
        Scanner input = new Scanner(System.in);
           
        //Backend only for now for testing
        System.out.println("Welcome! Please input string number 1 you want to compare:");
        String input1 = input.nextLine();
        String[] comp1; 
        comp1 = new String[input1.length()];

        System.out.println("Please input string number 2 you want to compare:");
        String input2 = input.nextLine();
        String[] comp2; 
        comp2 = new String[input2.length()]; 


        //Fill each array index[i] with corresponding letter in index[i] from string input1
        for (int i = 0; i != input1.length(); i++) {
            comp1[i] = input1.substring(i);
        } 
        //Same thing but for second array - for future use in ease of comparing.
        for (int i = 0; i != input2.length(); i++) {
            comp2[i] = input2.substring(i);
        } 
        System.out.println("Calculating...");



    }
}
