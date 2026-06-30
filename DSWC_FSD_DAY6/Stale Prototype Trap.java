// ReportTask.java
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class ReportTask {
    private final String taskId;
    private String data;
    
    public ReportTask() {
        this.taskId = "TASK-" + System.currentTimeMillis() + "-" + 
                       Thread.currentThread().getId();
        System.out.println("ReportTask created: " + taskId);
    }
    
    public void processData(String data) {
        this.data = data;
        System.out.println("Task " + taskId + " processing: " + data);
    }
    
    public String getTaskId() {
        return taskId;
    }
    
    public String getData() {
        return data;
    }
}

// ReportManager.java (The FIX)
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.stereotype.Service;

@Service
public class ReportManager {
    
    private final ObjectFactory<ReportTask> taskFactory;
    
    public ReportManager(ObjectFactory<ReportTask> taskFactory) {
        this.taskFactory = taskFactory;
        System.out.println("ReportManager initialized with ObjectFactory");
    }
    
    public void generateReport(String reportData) {
        // Get fresh task instance for each call
        ReportTask task = taskFactory.getObject();
        System.out.println("Generating report with task: " + task.getTaskId());
        task.processData(reportData);
        
        // Simulate processing
        System.out.println("Report generated with data: " + task.getData());
    }
}

// ReportApplication.java
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class ReportApplication {
    public static void main(String[] args) {
        ConfigurableApplicationContext context = 
            SpringApplication.run(ReportApplication.class, args);
        
        ReportManager manager = context.getBean(ReportManager.class);
        
        System.out.println("\n=== Generating Reports (Concurrent Scenario) ===\n");
        
        // Simulate multiple requests
        manager.generateReport("User Report Q1 2024");
        manager.generateReport("User Report Q2 2024");
        manager.generateReport("User Report Q3 2024");
        
        context.close();
    }
}