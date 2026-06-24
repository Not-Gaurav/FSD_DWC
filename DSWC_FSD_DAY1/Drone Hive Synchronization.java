import java.util.concurrent.atomic.AtomicInteger;

public class DroneHive {
    private AtomicInteger totalDronesReturned = new AtomicInteger(0);
    private volatile boolean emergencyAbort = false;
    private static final int TOTAL_DRONES = 10000;
    
    public void droneLanded() {
        totalDronesReturned.incrementAndGet();
    }
    
    public void setEmergencyAbort(boolean abort) {
        emergencyAbort = abort;
    }
    
    public boolean isEmergencyAbort() {
        return emergencyAbort;
    }
    
    public int getReturnedCount() {
        return totalDronesReturned.get();
    }
    
    public static void main(String[] args) throws InterruptedException {
        DroneHive hive = new DroneHive();
        Thread[] drones = new Thread[TOTAL_DRONES];
        
        for (int i = 0; i < TOTAL_DRONES; i++) {
            drones[i] = new Thread(() -> {
                hive.droneLanded();
                if (hive.isEmergencyAbort()) {
                    System.out.println("Drone rerouting due to emergency abort");
                }
            });
            drones[i].start();
        }
        
        Thread radar = new Thread(() -> {
            try {
                Thread.sleep(100);
                hive.setEmergencyAbort(true);
                System.out.println("Emergency abort triggered");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
        radar.start();
        
        for (Thread drone : drones) {
            drone.join();
        }
        radar.join();
        
        System.out.println("Total drones returned: " + hive.getReturnedCount());
    }
}