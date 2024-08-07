package Software1Assignment.views;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

import Software1Assignment.exceptions.ValidationException;
import Software1Assignment.models.Inventory;
import Software1Assignment.models.Part;
import Software1Assignment.models.Product;
import static Software1Assignment.views.MainController.getModifiedProduct;

/**
 * This class controls the add and modify menus for products.
 * 
 * @author Michael Vonica michaelvonica@gmail.com
 */
public class ProductsController implements Initializable {
    // Dynamic page label.
    @FXML
    private Label ProductsPageLabel;
    
    // Product ID.
    @FXML
    private TextField ProductsIdField;
    
    // Product name.
    @FXML
    private TextField ProductsNameField;
    
    // Product max required inventory.
    @FXML
    private TextField ProductsMaxField;
    
    // Product min required inventory.
    @FXML
    private TextField ProductsMinField;
    
    // Product current inventory.
    @FXML
    private TextField ProductsInventoryField;
    
    // Product price.
    @FXML
    private TextField ProductsPriceField;
    
    // Parts search field.
    @FXML
    private TextField ProductPartsSearchField;
    
    // All parts table.
    @FXML
    private TableView<Part> ProductAllPartsTable;
    
    // All parts ID column.
    @FXML
    private TableColumn<Part, Integer> ProductAllPartsIdCol;
    
    // All parts name column.
    @FXML
    private TableColumn<Part, String> ProductAllPartsNameCol;
    
    // All parts inventory column.
    @FXML
    private TableColumn<Part, Integer> ProductAllPartsInventoryCol;
    
    // All parts price column.
    @FXML
    private TableColumn<Part, Double> ProductAllPartsPriceCol;
    
    // Current Parts Table.
    @FXML
    private TableView<Part> ProductCurrentPartsTable;
    
    // Current Parts ID column.
    @FXML
    private TableColumn<Part, Integer> ProductCurrentPartsIdCol;
    
    // Current parts name column.
    @FXML
    private TableColumn<Part, String> ProductCurrentPartsNameCol;
    
    // Current parts inventory column.
    @FXML
    private TableColumn<Part, Integer> ProductCurrentPartsInventoryCol;
    
    // Current parts price column.
    @FXML
    private TableColumn<Part, Double> ProductCurrentPartsPriceCol;
    
    // List of parts associated with this project.
    private ObservableList<Part> productParts = FXCollections.observableArrayList();
    
    // Product being modified if this is a modification, else null.
    private final Product modifyProduct;
    
    /**
     * Constructor.
     */
    public ProductsController() {
        this.modifyProduct = getModifiedProduct();
    }
    
    /**
     * Add a part to the product.
     * 
     * @param event 
     */
    @FXML
    void handleAddProductPart(ActionEvent event) {
        Part part = ProductAllPartsTable.getSelectionModel().getSelectedItem();
        if (part != null) {
            productParts.add(part);
            populateCurrentPartsTable();
        }
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
        alert.setContentText("Are you sure you want to cancel update of product " + ProductsNameField.getText() + "?");
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
     * Delete a product part.
     * 
     * @param event
     * @throws IOException 
     */
    @FXML
    void handleDeleteProductPart(ActionEvent event) throws IOException {
        try {
            Part part = ProductCurrentPartsTable.getSelectionModel().getSelectedItem();
            if (part == null) {
                    throw new IOException("No part selected.");
            }
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.initModality(Modality.NONE);
            alert.setTitle("Part Delete");
            alert.setHeaderText("Confirm Deletion");
            alert.setContentText("Are you sure you want to disassociate " + part.getName() + "?");
            Optional<ButtonType> result = alert.showAndWait();

            if (result.get() == ButtonType.OK) {
                productParts.remove(part);
            }
        } catch (IOException e) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("IO Exception");
                alert.setHeaderText("Can Not Delete Part");
                alert.setContentText(e.getMessage());
                alert.showAndWait();
        }
    }
    
    /**
     * Part search handler. Update the parts table with items matching the input
     * field value. Render a modal if no parts match.
     * 
     * @param field
     * @param table 
     */
    @FXML
    void handleSearchPart(ActionEvent event) throws IOException {
        String partsSearchString = ProductPartsSearchField.getText();
        if (partsSearchString.equals("")) {
            populateAvailablePartsTable();
        }
        else {
            ObservableList<Part> filteredPartsList = FXCollections.observableArrayList();
            Part searchedPart = null;
            try {
                searchedPart = Inventory.lookupPart(Integer.parseInt(partsSearchString));
            } catch (NumberFormatException e) {
            }
            if (searchedPart != null) {
                filteredPartsList.add(searchedPart);
                ProductAllPartsTable.setItems(filteredPartsList);
                return;
            }
            filteredPartsList = Inventory.lookupPart(partsSearchString);
            if (filteredPartsList.size() > 0) {
                ProductAllPartsTable.setItems(filteredPartsList);
            }
            else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Search Error");
                alert.setHeaderText("Part Not Found");
                alert.setContentText("The search term entered does not match any part.");
                alert.showAndWait();
            }
        }
    }
    
    /**
     * Check to see if the provided product fulfills the necessary requirements.
     * If not, throw a Validation Exception.
     * 
     * @param modifiedProduct
     * @throws ValidationException 
     * @return true
     */
    public boolean isValid(Product modifiedProduct) throws ValidationException {
        // Name is required.
        if (modifiedProduct.getName().equals("")) {
            throw new ValidationException("The name field cannot be empty.");
        }
        
        // Inventory must be positive.
        if (modifiedProduct.getStock() < 0) {
            throw new ValidationException("The current inventory must be greater than\nor equal to 0.");
        }
        
        // Part price must be positive.
        if (modifiedProduct.getPrice() < 0) {
            throw new ValidationException("The price must be greater than or equal to 0.");
        }
        
        // The minimum must be positive.
        if (modifiedProduct.getMin() < 0) {
            throw new ValidationException("The minimum inventory must be greater than\nor equal to 0.");
        }
        
        // The maximum must be greater than the minimum.
        if (modifiedProduct.getMin() > modifiedProduct.getMax()) {
            throw new ValidationException("The minimum inventory must be less than\nor equal to the maximum.");
        }
        
        // The in stock inventory must be between min and max.
        if (modifiedProduct.getStock() < modifiedProduct.getMin() || modifiedProduct.getStock() > modifiedProduct.getMax()) {
            throw new ValidationException("The current inventory must be between the\nminimum and maximum inventory.");
        }
        
        return true;
    }

    /**
     * Handle a save event. This saves a new product or updates an existing one
     * based on this.modifyProduct.
     * 
     * @param event
     * @throws IOException 
     */
    @FXML
    void handleSave(ActionEvent event) throws IOException {
        String productName = ProductsNameField.getText();
        String productInv = ProductsInventoryField.getText();
        String productPrice = ProductsPriceField.getText();
        String productMin = ProductsMinField.getText();
        String productMax = ProductsMaxField.getText();

        Product modifiedProduct = new Product(0,
                    productName,
                    Double.parseDouble(productPrice),
                    Integer.parseInt(productInv),
                    Integer.parseInt(productMin),
                    Integer.parseInt(productMax));
       
        // If this is a modification, remove any parts currently associated with the product from the new list of associated parts, to prevent them from being added twice
        // This is basically a preemptive dedupe.
        //if (modifyProduct != null) {
        //    for (Part p: modifyProduct.getAllAssociatedParts()) {
        //        productParts.remove(p);
        //    }
        //}
        
        // Iterate productParts and add them to the product.
        for (Part p: productParts) {
            modifiedProduct.addAssociatedPart(p);
        }
        
        try {
            isValid(modifiedProduct);
            
            // Create or update product as required.
            if (modifyProduct == null) {
                modifiedProduct.setId(Inventory.getNextId());
                Inventory.increaseId();
                Inventory.addProduct(modifiedProduct);
            } else {
                modifiedProduct.setId(modifyProduct.getId());
                Inventory.updateProduct(modifiedProduct);
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
            alert.setHeaderText("Product Not Valid");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }
    
    /**
     * Initializes the controller class.
     * 
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // If modifiedProduct is null, we prepare the view in the add format.
        // Otherwise we populate existing data in the modify format,
        if (modifyProduct == null) {
            ProductsPageLabel.setText("Add Product");
            int productAutoId = Inventory.getNextId();
            ProductsIdField.setText("AUTO GEN: " + productAutoId);
        }
        else {
            ProductsPageLabel.setText("Modify Product");
            ProductsIdField.setText(Integer.toString(modifyProduct.getId()));
            ProductsNameField.setText(modifyProduct.getName());
            ProductsInventoryField.setText(Integer.toString(modifyProduct.getStock()));
            ProductsPriceField.setText(Double.toString(modifyProduct.getPrice()));
            ProductsMinField.setText(Integer.toString(modifyProduct.getMin()));
            ProductsMaxField.setText(Integer.toString(modifyProduct.getMax()));
            productParts = modifyProduct.getAllAssociatedParts();
        }
        
        ProductAllPartsIdCol.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getId()).asObject());
        ProductAllPartsNameCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
        ProductAllPartsInventoryCol.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getStock()).asObject());
        ProductAllPartsPriceCol.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getPrice()).asObject());
        
        ProductCurrentPartsIdCol.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getId()).asObject());
        ProductCurrentPartsNameCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
        ProductCurrentPartsInventoryCol.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getStock()).asObject());
        ProductCurrentPartsPriceCol.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getPrice()).asObject());
        
        // Populate the associated tables.
        populateAvailablePartsTable();
        populateCurrentPartsTable();
    }
    
    /**
     * Populate the available parts table.
     */
    public void populateAvailablePartsTable() {
        ProductAllPartsTable.setItems(Inventory.getAllParts());
    }

    /**
     * Populate the current parts table.
     */
    public void populateCurrentPartsTable() {
        ProductCurrentPartsTable.setItems(productParts);
    }
}