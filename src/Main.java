import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class Main extends Application {
	
    private BorderPane rootLayout;
	
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        try {
    		FXMLLoader loader = new FXMLLoader(getClass().getResource("UI.fxml"));
            rootLayout = loader.load();
            Scene scene = new Scene(rootLayout);     
            primaryStage.setScene(scene);
            
           UIController controller = loader.getController();
           controller.setCanvas((Pane) rootLayout.getChildren().get(3));
            
            primaryStage.setTitle("BST Visualization");
            primaryStage.show();
        } catch (Exception e) {
        	e.printStackTrace();
        }
    }
    
}
