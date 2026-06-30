// ImageRenderingEngine.java
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
@Lazy
public class ImageRenderingEngine {
    
    private boolean initialized = false;
    
    public ImageRenderingEngine() {
        System.out.println("ImageRenderingEngine: Heavy initialization (Lazy)");
        // Simulate heavy initialization
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        initialized = true;
        System.out.println("ImageRenderingEngine: Initialized successfully");
    }
    
    public void renderImage(String scanId) {
        System.out.println("Rendering image for scan: " + scanId);
    }
    
    public boolean isInitialized() {
        return initialized;
    }
}

// PatientContext.java
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class PatientContext {
    private final String patientId;
    private String diagnosticState;
    private boolean processed = false;
    
    public PatientContext() {
        this.patientId = "PAT-" + System.currentTimeMillis() + "-" + 
                          Thread.currentThread().getId();
        this.diagnosticState = "INITIAL";
        System.out.println("PatientContext created: " + patientId);
    }
    
    public void setDiagnosticState(String state) {
        this.diagnosticState = state;
    }
    
    public String getPatientId() {
        return patientId;
    }
    
    public String getDiagnosticState() {
        return diagnosticState;
    }
    
    public void markProcessed() {
        this.processed = true;
    }
    
    public boolean isProcessed() {
        return processed;
    }
}

// ScanOrchestrator.java
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ScanOrchestrator {
    
    private final ImageRenderingEngine renderingEngine;
    private final ObjectProvider<PatientContext> patientContextProvider;
    
    public ScanOrchestrator(ImageRenderingEngine renderingEngine,
                            ObjectProvider<PatientContext> patientContextProvider) {
        this.renderingEngine = renderingEngine;
        this.patientContextProvider = patientContextProvider;
        System.out.println("ScanOrchestrator: Initialized");
        System.out.println("RenderingEngine initialized: " + 
                           renderingEngine.isInitialized());
    }
    
    public void processScan(String scanId) {
        System.out.println("\n=== Processing Scan: " + scanId + " ===");
        
        // Get fresh PatientContext instance for each scan
        PatientContext context = patientContextProvider.getObject();
        System.out.println("PatientContext created: " + context.getPatientId());
        
        // Heavy rendering happens only when needed
        renderingEngine.renderImage(scanId);
        
        // Update context state
        context.setDiagnosticState("PROCESSED");
        context.markProcessed();
        
        System.out.println("Scan processing complete for: " + scanId);
        System.out.println("Context State: " + context.getDiagnosticState());
        System.out.println("Context ID: " + context.getPatientId());
        System.out.println("Processed: " + context.isProcessed());
        
        // Note: Different threads get different contexts
        System.out.println("Thread ID: " + Thread.currentThread().getId());
    }
}

// RadiologyApplication.java
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class RadiologyApplication {
    public static void main(String[] args) {
        ConfigurableApplicationContext context = 
            SpringApplication.run(RadiologyApplication.class, args);
        
        System.out.println("=== Application Started ===");
        
        ScanOrchestrator orchestrator = context.getBean(ScanOrchestrator.class);
        
        // Simulate multiple scan processing
        System.out.println("\n--- Processing First Scan ---");
        orchestrator.processScan("MRI-001");
        
        System.out.println("\n--- Processing Second Scan ---");
        orchestrator.processScan("MRI-002");
        
        System.out.println("\n--- Processing Third Scan ---");
        orchestrator.processScan("CT-003");
        
        context.close();
    }
}