package Software1Assignment.models;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Inventory is a list with access and control over all parts and products.
 * 
 * @author Michael Vonica michaelvonica@gmail.com
 */
public class Inventory {
    private static final ObservableList<Part> allParts = FXCollections.observableArrayList();
    private static final ObservableList<Product> allProducts = FXCollections.observableArrayList();
    private static int nextId = 0;
    
    /**
     * Constructor.
     */
    public Inventory() {
    }
    
    /**
     * Get the next part ID number.
     * 
     * @return next part ID.
     */
    public static int getNextId() {
        return nextId;
    }
    
    /**
     * Increase the value of the next part ID.
     */
    public static void increaseId() {
        nextId++;
    }
    
    /**
     * Add a new part to the inventory.
     * 
     * @param newPart 
     */
    public static void addPart(Part newPart){
        allParts.add(newPart);
    }
    
    /**
     * Add a new product to the inventory.
     * @param newProduct 
     */
    public static void addProduct(Product newProduct){
        allProducts.add(newProduct);
    }
    
    /**
     * Look up a part by ID.
     * 
     * @param partID
     * @return Part if applicable, else null
     */
    public static Part lookupPart(int partID) {
        for (Part p : allParts) {
            if (p.getId() == partID) {
                return p;
            }
        }

        return null;
    }
    
    /**
     * Look up a product by ID.
     * 
     * @param productID
     * @return Product if applicable, else null
     */
    public static Product lookupProduct(int productID) {
        for (Product p : allProducts) {
            if (p.getId() == productID) {
                return p;
            }
        }
        
        return null;
    }
    
    /**
     * Look up all parts that match the given name.
     * 
     * @param Name
     * @return Observable list of parts
     */
    public static ObservableList<Part> lookupPart(String Name) {
        ObservableList<Part> searchResults = FXCollections.observableArrayList();
        for (Part p : allParts) {
            if (p.getName().matches(".*"+Name+(".*"))) {
                searchResults.add(p);
            }
        }

        return searchResults;
    }
    
    /**
     * Look up all products that match the given name.
     * 
     * @param Name
     * @return Observable list of products
     */
    public static ObservableList<Product> lookupProduct(String Name) {
        ObservableList<Product> searchResults = FXCollections.observableArrayList();
        for (Product p : allProducts) {
            if (p.getName().matches(".*"+Name+(".*"))) {
                searchResults.add(p);
            }
        }

        return searchResults;
    }
    
    /**
     * Update a part in the inventory.
     * 
     * @param updatedPart 
     */
    public static boolean updatePart(Part updatedPart) {
        allParts.set(updatedPart.getId(), updatedPart);
        for (Part p : allParts) {
            if (p.getId() == updatedPart.getId()) {
                allParts.set(allParts.indexOf(p), updatedPart);
                return true;
            }
        }
        
        return false;
    }
    
    /**
     * Update a product in the inventory.
     * 
     * @param updatedProduct 
     */
    public static boolean updateProduct(Product updatedProduct) {
        for (Product p : allProducts) {
            if (p.getId() == updatedProduct.getId()) {
                allProducts.set(allProducts.indexOf(p), updatedProduct);
                return true;
            }
        }
        
        return false;
    }
    
    /**
     * Delete a part.
     * 
     * @param partID
     * @return true if the product was removed successfully, else false
     */
    public static boolean deletePart(int partID) {
        for (Part p : allParts) {
            if (p.getId() == partID) {
                allParts.remove(p);
                return true;
            }
        }
        
        return false;
    }

    /**
     * Delete a product.
     * 
     * @param productID
     * @return true if the product was removed successfully, else false
     */
    public static boolean deleteProduct(int productID) {
        for (Product p : allProducts) {
            if (p.getId() == productID) {
                allProducts.remove(p);
                return true;
            }
        }
        
        return false;
    }
    
    /**
     * Get a list of all parts.
     * 
     * @return list of parts in inventory
     */
    public static ObservableList<Part> getAllParts() {
        return allParts;
    }
    
    /**
     * Get a list of all products.
     * 
     * @return list of products in inventory
     */
    public static ObservableList<Product> getAllProducts() {
        return allProducts;
    }
}