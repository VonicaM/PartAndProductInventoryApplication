package Software1Assignment.views;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

import Software1Assignment.models.InHouse;
import Software1Assignment.models.Inventory;
import Software1Assignment.models.Outsourced;
import Software1Assignment.models.Part;
import static Software1Assignment.views.MainController.getModifiedPart;
import Software1Assignment.exceptions.ValidationException;

/**
 * This class controls the parts screen that allows for the adding and modifying
 * of parts.
 * 
 * @author Michael Vonica michaelvonica@gmail.com
 */
public class PartsController implements Initializable {
    // GUI part ID.
    @FXML
    private TextField PartsIdField;
    
    // GUI part common name.
    @FXML
    private TextField PartsNameField;
    
    // GUI part current inventory.
    @FXML
    private TextField PartsInventoryField;
    
    // GUI part price.
    @FXML
    private TextField PartsPriceField;
    
    // GUI part max required inventory.
    @FXML
    private TextField PartsMaxField;
    
    // GUI part min required inventory.
    @FXML
    private TextField PartsMinField;
    
     // GUI variable manufacturer label (inhouse vs outsourced).
    @FXML
    private Label PartsMfgLabel;

    // GUI part manufacturer ID.
    @FXML
    private TextField PartsMfgField;
    
    // GUI page label.
    @FXML
    private Label PartsPageLabel;
    
    // GUI inhouse radio button.
    @FXML
    private RadioButton PartsInHouseRadioButton;
    
    // GUI outsourced radio button.
    @FXML
    private RadioButton PartsOutsourcedRadioButton;
    
    // Flag representing the manufacturing status of this part.
    // Inhouse or outsourced.
    private boolean isInHouse;
    
    // Part being modified if this is a modification, else null. 
    private final Part modifyPart;
    
    /**
     * Constructor.
     */
    public PartsController() {
        this.modifyPart = getModifiedPart();
    }
    
    /**
     * Handle a switch to inhouse. Update the instance data store and GUI to
     * reflect a change to inhouse.
     * 
     * @param event 
     */
    @FXML
    void handleInHouse(ActionEvent event) {
        isInHouse = true;
        PartsMfgLabel.setText("Machine ID");
    }
    
    /**
     * Handle a switch to outsourced. Update the instance data store and GUI to 
     * reflect a change to outsourced.
     * 
     * @param event 
     */
    @FXML
    void handleOutsource(ActionEvent event) {
        isInHouse = false;
        PartsMfgLabel.setText("Company Name");
    }
    
    /**
     * Handle a cancel event. This requires the user confirm intent to cancel 
     * add/modify, and returns them to the main view as required.
     * 
     * @param event
     * @throws IOException 
     */
    @FXML
    void handleCancel(ActionEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initModality(Modality.NONE);
        alert.setTitle("Cancel Modification");
        alert.setHeaderText("Confirm Cancellation");
        alert.setContentText("Are you sure you want to cancel update of part " + PartsNameField.getText() + "?");
        Optional<ButtonType> result = alert.showAndWait();
        
        if (result.get() == ButtonType.OK) {
            Parent loader = FXMLLoader.load(getClass().getResource("Main.fxml"));
            Scene scene = new Scene(loader);
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setScene(scene);
            window.show();
        }
    }
    
    /**
     * Check to see if the provided part fulfills the necessary requirements.
     * If not, throw a Validation Exception.
     * 
     * @param modifiedPart
     * @throws ValidationException 
     * @return true
     */
    public boolean isValid(Part modifiedPart) throws ValidationException {
        // Name is required.
        if (modifiedPart.getName().equals("")) {
            throw new ValidationException("The name field cannot be empty.");
        }
        
        // Inventory must be positive.
        if (modifiedPart.getStock() < 0) {
            throw new ValidationException("The current inventory must be greater than\nor equal to 0.");
        }
        
        // Part price must be positive.
        if (modifiedPart.getPrice() < 0) {
            throw new ValidationException("The price must be greater than or equal to 0.");
        }
        
        // The minimum must be positive.
        if (modifiedPart.getMin() < 0) {
            throw new ValidationException("The minimum inventory must be greater than\nor equal to 0.");
        }
        
        // The maximum must be greater than the minimum.
        if (modifiedPart.getMin() > modifiedPart.getMax()) {
            throw new ValidationException("The minimum inventory must be less than\nor equal to the maximum.");
        }
        
        // The in stock inventory must be between min and max.
        if (modifiedPart.getStock() < modifiedPart.getMin() || modifiedPart.getStock() > modifiedPart.getMax()) {
            throw new ValidationException("The current inventory must be between the\nminimum and maximum inventory.");
        }
        
        return true;
    }
    
    /**
     * Handle a save event. This saves a new part or updates an existing one
     * based on this.modifiedPart.
     * 
     * @param event
     * @throws IOException 
     */
    @FXML
    void handleSave(ActionEvent event) throws IOException {
        // Get data from the GUI.
        String partName = PartsNameField.getText();
        String partInv = PartsInventoryField.getText();
        String partPrice = PartsPriceField.getText();
        String partMin = PartsMinField.getText();
        String partMax = PartsMaxField.getText();
        String partDyn = PartsMfgField.getText();
        
        if ("".equals(partInv)) {
            partInv = "0";
        }
        
        if (isInHouse) {
            // Create or modify an instance of InHouse and set the instance.
            InHouse modifiedPart = new InHouse(0,
                    partName,
                    Double.parseDouble(partPrice),
                    Integer.parseInt(partInv),
                    Integer.parseInt(partMin),
                    Integer.parseInt(partMax),
                    Integer.parseInt(partDyn));
            
            try {
                isValid(modifiedPart);
                
                // If this is a modified part, update it. Otherwise save a new part.
                if (modifyPart == null) {
                    modifiedPart.setId(Inventory.getNextId());
                    Inventory.increaseId();
                    Inventory.addPart(modifiedPart);
                } else {
                    modifiedPart.setId(modifyPart.getId());
                    Inventory.updatePart(modifiedPart);
                }
                
                // Return to the main screen.
                Parent loader = FXMLLoader.load(getClass().getResource("Main.fxml"));
                Scene scene = new Scene(loader);
                Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
                window.setScene(scene);
                window.show();
            } catch (ValidationException e) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Validation Error");
                alert.setHeaderText("Part Not Valid");
                alert.setContentText(e.getMessage());
                alert.showAndWait();
            }
        } else {
            // Create or modify an instance of Outsourced and set the instance.
            Outsourced modifiedPart = new Outsourced(0,
                    partName,
                    Double.parseDouble(partPrice),
                    Integer.parseInt(partInv),
                    Integer.parseInt(partMin),
                    Integer.parseInt(partMax),
                    partDyn);
            
            try {
                isValid(modifiedPart);
                
                // If this is a modified part, update it. Otherwise save a new part.
                if (modifyPart == null) {
                    modifiedPart.setId(Inventory.getNextId());
                    Inventory.increaseId();
                    Inventory.addPart(modifiedPart);
                } else {
                    modifiedPart.setId(modifyPart.getId());
                    Inventory.updatePart(modifiedPart);
                }
                
                // Return to the main screen.
                Parent loader = FXMLLoader.load(getClass().getResource("Main.fxml"));
                Scene scene = new Scene(loader);
                Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
                window.setScene(scene);
                window.show();
            } catch (ValidationException e) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Validation Error");
                alert.setHeaderText("Part Not Valid");
                alert.setContentText(e.getMessage());
                alert.showAndWait();
            }
        }
    }
    
    /**
     * Initialize the class. This is done dynamically through the modifyPart
     * instance variable. If modifyPart is not null (meaning that a part is
     * being modified), we will use its data to seed the GUI.
     * 
     * @param url
     * @param rb 
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        if (modifyPart == null) {
            PartsPageLabel.setText("Add Part");
            int partAutoId = Inventory.getNextId();
            PartsIdField.setText("AUTO GEN: " + partAutoId);
            isInHouse = true;
            PartsMfgLabel.setText("Machine ID");
        }
        else{
            PartsPageLabel.setText("Modify Part");
            PartsIdField.setText(Integer.toString(modifyPart.getId()));
            PartsNameField.setText(modifyPart.getName());
            PartsInventoryField.setText(Integer.toString(modifyPart.getStock()));
            PartsPriceField.setText(Double.toString(modifyPart.getPrice()));
            PartsMinField.setText(Integer.toString(modifyPart.getMin()));
            PartsMaxField.setText(Integer.toString(modifyPart.getMax()));
            
            // Since modifyPart belongs to the subclass, we have to cast it to
            // the appropriate super class.
            if (modifyPart instanceof InHouse) {
                PartsMfgField.setText(Integer.toString(((InHouse) modifyPart).getMachineId()));
                PartsMfgLabel.setText("Machine ID");
                PartsInHouseRadioButton.setSelected(true);

            } else {
                PartsMfgField.setText(((Outsourced) modifyPart).getCompanyName());
                PartsMfgLabel.setText("Company Name");
                PartsOutsourcedRadioButton.setSelected(true);
            }
        }
    }
}