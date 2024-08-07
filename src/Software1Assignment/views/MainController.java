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
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

import Software1Assignment.Software1Assignment;
import Software1Assignment.models.Inventory;
import static Software1Assignment.models.Inventory.deletePart;
import static Software1Assignment.models.Inventory.deleteProduct;
import static Software1Assignment.models.Inventory.getAllParts;
import static Software1Assignment.models.Inventory.getAllProducts;
import Software1Assignment.models.Part;
import Software1Assignment.models.Product;

/**
 * This class controls the main inventory screen containing lists of parts
 * and products.
 * 
 * @author Michael Vonica michaelvonica@gmail.com
 */
public class MainController implements Initializable {
    // The whole parts table.
    @FXML
    private TableView<Part> MainPartsTable;
    
    // Parts table ID column.
    @FXML
    private TableColumn<Part, Integer> MainPartIdCol;
    
    // Parts table name column.
    @FXML
    private TableColumn<Part, String> MainPartNameCol;
    
    // Parts table current inventory column.
    @FXML
    private TableColumn<Part, Integer> MainPartInventoryCol;
    
    // Parts table price column.
    @FXML
    private TableColumn<Part, Double> MainPartPriceCol;
    
    // The whole products table.
    @FXML
    private TableView<Product> MainProductsTable;
    
    // Products table Id column.
    @FXML
    private TableColumn<Product, Integer> MainProductIdCol;
    
    // Products table name column.
    @FXML
    private TableColumn<Product, String> MainProductNameCol;
    
    // Products table current inventory column.
    @FXML
    private TableColumn<Product, Integer> MainProductInventoryCol;
    
    // Products table price column.
    @FXML
    private TableColumn<Product, Double> MainProductPriceCol;
    
    // Parts search field.
    @FXML
    private TextField MainPartsSearchField;
    
    // Products search field.
    @FXML
    private TextField MainProductsSearchField;

    // The current modified part (if applicable).
    private static Part modifiedPart;
    
    // The current modified product (if applicable).
    private static Product modifiedProduct;
    
    /**
     * Constructor.
     */
    public MainController() {
    }
    
    /**
     * Get the current modified part.
     * 
     * @return current modified part
     */
    public static Part getModifiedPart() {
        return modifiedPart;
    }
    
    /**
     * Set a part as current modified.
     * 
     * @param modifyPart 
     */
    public void setModifiedPart(Part modifyPart) {
        MainController.modifiedPart = modifyPart;
    }
    
    /**
     * Get the current modified product.
     * 
     * @return current modified product
     */
    public static Product getModifiedProduct() {
        return modifiedProduct;
    }
    
    /**
     * Set a product as current modified.
     * 
     * @param modifiedProduct 
     */
    public void setModifiedProduct(Product modifiedProduct) {
        MainController.modifiedProduct = modifiedProduct;
    }
    
    /**
     * Handle exit. Render a confirmation modal and close GUI as applicable.
     * 
     * @param event 
     */
    @FXML
    void handleExit(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initModality(Modality.NONE);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Confirm Exit");
        alert.setContentText("Are you sure you want to exit?");
        Optional<ButtonType> result = alert.showAndWait();

        if (result.get() == ButtonType.OK) {
            System.exit(0);
        }
    }
    
    /**
     * Switch to the add parts screen.
     * 
     * @param event
     * @throws IOException 
     */
    @FXML
    void handleAddPart(ActionEvent event) throws IOException {        
        showPartsScreen(event);
    }
    
    /**
     * Switch to the add product screen.
     * 
     * @param event
     * @throws IOException 
     */
    @FXML
    void handleAddProduct(ActionEvent event) throws IOException {
        showProductScreen(event);
    }

    /**
     * Handle the deletion of a part.
     * 
     * @param event
     * @throws IOException 
     */
    @FXML
    void handleDeletePart(ActionEvent event) throws IOException {
        try {
            Part part = MainPartsTable.getSelectionModel().getSelectedItem();
            if (part == null) {
                throw new IOException("No part selected.");
            }
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.initModality(Modality.NONE);
            alert.setTitle("Part Delete");
            alert.setHeaderText("Confirm Deletion");
            alert.setContentText("Are you sure you want to delete " + part.getName() + "?");
            Optional<ButtonType> result = alert.showAndWait();

            if (result.get() == ButtonType.OK) {
                deletePart(part.getId());
                populatePartsTable();
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
     * Delete a product.
     * 
     * @param event
     * @throws IOException 
     */
    @FXML
    void handleDeleteProduct(ActionEvent event) throws IOException {
        try {
            Product product = MainProductsTable.getSelectionModel().getSelectedItem();
            if (product == null) {
                    throw new IOException("No product selected.");
            }
            if (product.getAllAssociatedParts().isEmpty()) {
                Alert alert = new Alert(AlertType.CONFIRMATION);
                alert.initModality(Modality.NONE);
                alert.setTitle("Product Delete");
                alert.setHeaderText("Confirm Deletion");
                alert.setContentText("Are you sure you want to delete " + product.getName() + "?");
                Optional<ButtonType> result = alert.showAndWait();

                if (result.get() == ButtonType.OK) {
                    deleteProduct(product.getId());
                    populatePartsTable();
                }
            }
            else {
                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Produt Deletion Error");
                alert.setHeaderText("Produt Cannot Be Removed");
                alert.setContentText("This product has associated parts.");
                alert.showAndWait();
            }
        } catch (IOException e) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("IO Exception");
                alert.setHeaderText("Can Not Delete Product");
                alert.setContentText(e.getMessage());
                alert.showAndWait();
        }
    }
    
    /**
     * Using the currently selected part, go to the modify part screen.
     * 
     * @param event
     * @throws IOException 
     */
    @FXML
    void handleModifyPart(ActionEvent event) throws IOException {
        modifiedPart = MainPartsTable.getSelectionModel().getSelectedItem();
        if (modifiedPart == null) {
        }
        else {
            setModifiedPart(modifiedPart);
            showPartsScreen(event);
        }
    }

    /**
     * Using the currently selected product, go to the modify product screen.
     * 
     * @param event
     * @throws IOException 
     */
    @FXML
    void handleModifyProduct(ActionEvent event) throws IOException {
        modifiedProduct = MainProductsTable.getSelectionModel().getSelectedItem();
        if (modifiedProduct == null) {
        }
        else {
            setModifiedProduct(modifiedProduct);
            showProductScreen(event);
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
        String partsSearchString = MainPartsSearchField.getText();
        if (partsSearchString.equals("")) {
            populatePartsTable();
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
                MainPartsTable.setItems(filteredPartsList);
                return;
            }
            filteredPartsList = Inventory.lookupPart(partsSearchString);
            if (filteredPartsList.size() > 0) {
                MainPartsTable.setItems(filteredPartsList);
            }
            else {
                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Search Error");
                alert.setHeaderText("Part Not Found");
                alert.setContentText("The search term entered does not match any part.");
                alert.showAndWait();
            }
        }
    }
    
    /**
     * Product search handler. Update the parts table with items matching the
     * input field value. Render a modal if no products match.
     * 
     * @param field
     * @param table 
     */
    @FXML
    void handleSearchProduct(ActionEvent event) throws IOException {
        String productsSearchString = MainProductsSearchField.getText();
        if (productsSearchString.equals("")) {
            populateProductsTable();
        }
        else {
            ObservableList<Product> filteredProductsList = FXCollections.observableArrayList();
            Product searchedProduct = null;
            try {
                searchedProduct = Inventory.lookupProduct(Integer.parseInt(productsSearchString));
            } catch (NumberFormatException e) {
            }
            if (searchedProduct != null) {
                filteredProductsList.add(searchedProduct);
                MainProductsTable.setItems(filteredProductsList);
                return;
            }
            filteredProductsList = Inventory.lookupProduct(productsSearchString);
            if (filteredProductsList.size() > 0) {
                MainProductsTable.setItems(filteredProductsList);
            }
            else {
                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Search Error");
                alert.setHeaderText("Product Not Found");
                alert.setContentText("The search term entered does not match any product.");
                alert.showAndWait();
            }
        }
    }
    
    /**
     * Initialize the controller.
     * 
     * @param url
     * @param rb 
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // initialize part and product with nulls
        setModifiedPart(null);
        setModifiedProduct(null);

        MainPartIdCol.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getId()).asObject());
        MainPartNameCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
        MainPartInventoryCol.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getStock()).asObject());
        MainPartPriceCol.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getPrice()).asObject());
        
        MainProductIdCol.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getId()).asObject());
        MainProductNameCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
        MainProductInventoryCol.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getStock()).asObject());
        MainProductPriceCol.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getPrice()).asObject());
        
        populatePartsTable();
        populateProductsTable();
    }
    
    /**
     * Populate the parts table.
     */
    public void populatePartsTable() {
        MainPartsTable.setItems(getAllParts());
    }

    /**
     * Populate the product table.
     */
    public void populateProductsTable() {
        MainProductsTable.setItems(getAllProducts());
    }
    
    /**
     * Set the main app. Populate the parts and products tables.
     * 
     * @param mainApp 
     */
    public void setMainApp(Software1Assignment mainApp) {
        populatePartsTable();
        populateProductsTable();
    }
    
    /**
     * Render the parts screen. Both add and modify parts functionality is
     * handled by the same view and controller, so we can make a generic handler
     * for it.
     * 
     * @param event
     * @throws IOException 
     */
    public void showPartsScreen(ActionEvent event) throws IOException {
        Parent loader = FXMLLoader.load(getClass().getResource("Parts.fxml"));
        Scene scene = new Scene(loader);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }
    
    /**
     * Render the products screen. Both add and modify products functionality is
     * handled by the same view and controller, so we can make a generic handler
     * for it.
     * 
     * @param event
     * @throws IOException 
     */
    public void showProductScreen(ActionEvent event) throws IOException {
        Parent loader = FXMLLoader.load(getClass().getResource("Products.fxml"));
        Scene scene = new Scene(loader);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }
}