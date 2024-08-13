package Software1Assignment.models;

/**
 * InHouse is a derivative of Part, adding a machine ID to the instance.
 * 
 * @see Part
 * @author Michael Vonica michaelvonica@gmail.com
 */
public class InHouse extends Part{
    private int machineID;
    public InHouse(int id, String name, double price, int stock, int min, int max, int machineId) {
        super(id, name, price, stock, min, max);
        this.machineID = machineId;
    }
    
    /**
     * @param machineId the machine id to set
     */
    public void setMachineId(int machineId) {
        this.machineID = machineId;
    }
    
    /**
     * @return the machine id
     */
    public int getMachineId() {
        return machineID;
    }
}