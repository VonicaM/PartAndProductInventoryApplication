/**
 * An error I faced and how I corrected it. dog
 * 
 * An error that occurred as I was creating this program involved problems with
 * generating unique IDs for the parts stored within the inventory. I first
 * attempted to create these IDs based on the current size of the list of parts,
 * but I found that, in cases such as where a user were to create two parts, the
 * first with an ID of 0 and the second with an ID of 1, then delete part ID 0
 * and attempt to create a new part, duplicate IDs would be created, as there
 * would be two parts with an ID of 1, as at the time of their creation, that
 * would be the number of parts in the list. To solve this problem, I created a
 * variable in the inventory class called nextId, which increments after every
 * creation of a part of product, and which is used at the unique ID for any
 * newly created parts or products. The use of this variable insures that all
 * parts and products have their own unique IDs, even if this unique nature is
 * shared across both parts and products, and if it leads to non contiguous IDs.
 */

/**
 * A future enhancement that would extend the functionality of the application.
 * 
 * A future enhancement that I believe could be implemented into the application
 * in order to improve its functionality would be to change how the search function
 * for the parts and products menus work, so that searching by name and searching
 * by ID are two separate functions. Currently, the search function will search
 * by ID, and if no matches are found, then it will search by name. This is somewhat
 * unintuitive, so I believe that by changing this process, the application will
 * become more user friendly.
 */

package Software1Assignment;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Inventory Application for C482, Software 1.
 * This application is an inventory application that allows the user to 
 * maintain an inventory through a JavaFX GUI.
 * 
 * @author Michael Vonica michaelvonica@gmail.com
 */
public class Software1Assignment extends Application{

    /**
     * Loads the main FXML and sets the GUI.
     * 
     * @param stage
     * @throws Exception 
     */
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Software1Assignment.class.getResource("/Software1Assignment/views/Main.fxml"));
        Parent root = loader.load();
    
        Scene scene = new Scene(root);
        
        stage.setScene(scene);
        stage.show();
    }
    
    /**
     * Launch the GUI.
     * 
     * @param args 
     */
    public static void main(String[] args) {
        launch(args);
    }
}
