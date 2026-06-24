// HardwareLockException.java
public class HardwareLockException extends Exception {
    public HardwareLockException(String message) {
        super(message);
    }
    
    public HardwareLockException(String message, Throwable cause) {
        super(message, cause);
    }
}

// SensorCorruptionException.java
public class SensorCorruptionException extends RuntimeException {
    public SensorCorruptionException(String message) {
        super(message);
    }
    
    public SensorCorruptionException(String message, Throwable cause) {
        super(message, cause);
    }
}

// TelemetryStream.java
public class TelemetryStream implements AutoCloseable {
    private boolean isOpen = true;
    
    public void readData() throws HardwareLockException {
        if (Math.random() > 0.5) {
            throw new HardwareLockException("Hardware lock detected");
        }
    }
    
    public void processSensor() {
        if (Math.random() > 0.7) {
            throw new SensorCorruptionException("Sensor corruption detected");
        }
    }
    
    @Override
    public void close() {
        isOpen = false;
        System.out.println("TelemetryStream closed");
    }
    
    public boolean isOpen() {
        return isOpen;
    }
}

// TelemetryParser.java
public class TelemetryParser {
    public void parseTelemetry() {
        try (TelemetryStream stream = new TelemetryStream()) {
            stream.readData();
            stream.processSensor();
        } catch (HardwareLockException e) {
            System.err.println("Fatal hardware error: " + e.getMessage());
        } catch (SensorCorruptionException e) {
            System.err.println("Recoverable sensor error: " + e.getMessage());
        }
    }
    
    public static void main(String[] args) {
        TelemetryParser parser = new TelemetryParser();
        for (int i = 0; i < 5; i++) {
            System.out.println("Attempt " + (i + 1) + ":");
            parser.parseTelemetry();
            System.out.println();
        }
    }
}