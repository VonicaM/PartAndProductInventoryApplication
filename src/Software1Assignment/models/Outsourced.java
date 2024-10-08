package Software1Assignment.models;

/**
 * Outsourced is a derivative of Part, adding a company name to the instance.
 * 
 * @see Part
 * @author Michael Vonica michaelvonica@gmail.com
 */
public class Outsourced extends Part{
    private String companyName;
    public Outsourced(int id, String name, double price, int stock, int min, int max, String companyName) {
        super(id, name, price, stock, min, max);
        this.companyName = companyName;
    }
    
    /**
     * @param companyName the companyName to set
     */
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
    
    /**
     * @return the company name
     */
    public String getCompanyName() {
        return companyName;
    }
}
