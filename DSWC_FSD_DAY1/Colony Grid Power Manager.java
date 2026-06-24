public class PowerManager {
    private byte sectorStates;
    
    public PowerManager() {
        sectorStates = 0;
    }
    
    public void turnOnSector(int sectorIndex) {
        if (sectorIndex >= 0 && sectorIndex < 8) {
            sectorStates = (byte) (sectorStates | (1 << sectorIndex));
        }
    }
    
    public void turnOffSector(int sectorIndex) {
        if (sectorIndex >= 0 && sectorIndex < 8) {
            sectorStates = (byte) (sectorStates & ~(1 << sectorIndex));
        }
    }
    
    public boolean isSectorOn(int sectorIndex) {
        if (sectorIndex >= 0 && sectorIndex < 8) {
            return (sectorStates & (1 << sectorIndex)) != 0;
        }
        return false;
    }
    
    public byte getSectorStates() {
        return sectorStates;
    }
    
    public static void main(String[] args) {
        PowerManager pm = new PowerManager();
        pm.turnOnSector(0);
        pm.turnOnSector(3);
        pm.turnOnSector(7);
        System.out.println("Sector states: " + Integer.toBinaryString(pm.getSectorStates() & 0xFF));
        System.out.println("Sector 3 on: " + pm.isSectorOn(3));
        pm.turnOffSector(3);
        System.out.println("Sector 3 on after turn off: " + pm.isSectorOn(3));
    }
}