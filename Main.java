import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        Parent rootNode = new MenuLayout(); // Construct custom layout as root node
        Scene scene = new Scene(rootNode);
        stage.setScene(scene);
        stage.setTitle("Maxim's Algorithm Visualizer");
        stage.show();
    }
}
