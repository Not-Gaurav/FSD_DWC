// SpaceVessel.java
public abstract class SpaceVessel {
    private short shipId;        // 30,000 fits in short (32,767 max)
    private boolean operationalStatus;
    private char fleetClassification; // 'A', 'B', or 'C'
    
    public SpaceVessel(short shipId, boolean operationalStatus, char fleetClassification) {
        this.shipId = shipId;
        this.operationalStatus = operationalStatus;
        this.fleetClassification = fleetClassification;
    }
    
    public short getShipId() { return shipId; }
    public boolean isOperational() { return operationalStatus; }
    public char getFleetClassification() { return fleetClassification; }
}

// MiningShip.java
public class MiningShip extends SpaceVessel {
    private float[][] cargoHold;  // float for decimal values up to 50,000.00
    private byte bayCount;
    
    public MiningShip(short shipId, boolean operationalStatus, char fleetClassification, byte bayCount, byte containersPerBay) {
        super(shipId, operationalStatus, fleetClassification);
        this.bayCount = bayCount;
        this.cargoHold = new float[bayCount][containersPerBay];
    }
    
    public void setOreWeight(int bay, int container, float weight) {
        cargoHold[bay][container] = weight;
    }
    
    public float calculateTotalOreWeight() {
        float total = 0.0f;
        for (int i = 0; i < cargoHold.length; i++) {
            for (int j = 0; j < cargoHold[i].length; j++) {
                total += cargoHold[i][j];
            }
        }
        return total;
    }
    
    public float findHeaviestContainer() {
        float heaviest = 0.0f;
        for (int i = 0; i < cargoHold.length; i++) {
            for (int j = 0; j < cargoHold[i].length; j++) {
                if (cargoHold[i][j] > heaviest) {
                    heaviest = cargoHold[i][j];
                }
            }
        }
        return heaviest;
    }
}

// Main.java
public class Main {
    public static void main(String[] args) {
        SpaceVessel[] fleet = new SpaceVessel[3];
        fleet[0] = new MiningShip((short)1001, true, 'A', (byte)5, (byte)10);
        fleet[1] = new MiningShip((short)1002, false, 'B', (byte)4, (byte)8);
        fleet[2] = new MiningShip((short)1003, true, 'C', (byte)6, (byte)12);
        
        MiningShip ship = (MiningShip) fleet[0];
        ship.setOreWeight(0, 0, 4500.50f);
        ship.setOreWeight(0, 1, 3200.75f);
        ship.setOreWeight(1, 0, 5000.00f);
        
        System.out.println("Total ore weight: " + ship.calculateTotalOreWeight());
        System.out.println("Heaviest container: " + ship.findHeaviestContainer());
    }
}