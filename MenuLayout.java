import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.InnerShadow;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;


/**
 * Custom layout based on VBox for a menu to select which algorithm to visualize.
 */
class MenuLayout extends VBox {
    /**
     * Basic constructor, initializes the menu with a button to each algorithm.
     */
    MenuLayout() {
        setAlignment(Pos.CENTER);

        Text title = new Text("Algorithm Visualizer");
        title.setFont(Font.font(24));
        
        FlowPane buttonsBox = new FlowPane();
        buttonsBox.setAlignment(Pos.CENTER);

        Button AESBtn = new Button("Advanced Encryption Standard algorithm");
        // AESBtn.setMinSize(300, 50);
        AESBtn.setOnAction(event -> FXUtils.setSceneRoot(getScene(), new AEStart()));
        AESBtn.getStyleClass().add("cool-button"); // Apply CSS class




        
        buttonsBox.getChildren().addAll(AESBtn);
        getChildren().addAll(title, buttonsBox);
        

    }
}
