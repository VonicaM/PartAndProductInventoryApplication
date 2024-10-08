package Software1Assignment.models;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Product is an item stored within the system, similar to parts. Products are
 * composed of parts.
 * 
 * @author Michael Vonica michaelvonica@gmail.com
 */
public class Product {
    private int id;
    private String name;
    private double price;
    private int stock;
    private int min;
    private int max;
    private ObservableList<Part> associatedParts = FXCollections.observableArrayList();
    public Product(int id, String name, double price, int stock, int min, int max) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.min = min;
        this.max = max;
    }
    
    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the price
     */
    public double getPrice() {
        return price;
    }

    /**
     * @param price the price to set
     */
    public void setPrice(double price) {
        this.price = price;
    }
    
    /**
     * @return the stock
     */
    public int getStock() {
        return stock;
    }

    /**
     * @param stock the stock to set
     */
    public void setStock(int stock) {
        this.stock = stock;
    }

    /**
     * @return the min
     */
    public int getMin() {
        return min;
    }

    /**
     * @param min the min to set
     */
    public void setMin(int min) {
        this.min = min;
    }

    /**
     * @return the max
     */
    public int getMax() {
        return max;
    }

    /**
     * @param max the max to set
     */
    public void setMax(int max) {
        this.max = max;
    }
    
    /**
     * Add an associated part.
     * 
     * @param associatedPart 
     */
    public void addAssociatedPart(Part associatedPart) {
        this.associatedParts.add(associatedPart);
    }
    
    /**
     * Delete an associated part.
     * 
     * @param selectedAssociatedPart
     * @return 
     */
    public boolean deleteAssociatedPart(Part selectedAssociatedPart) {
        for (Part p : associatedParts) {
            if (p.getId() == selectedAssociatedPart.getId()) {
                associatedParts.remove(p);
                return true;
            }
        }
        return false;
    }
    
    /**
     * Get a list of all associated parts.
     * 
     * @return associated parts
     */
    public ObservableList<Part> getAllAssociatedParts() {
        return associatedParts;
    }
}