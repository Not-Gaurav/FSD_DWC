// PaymentProcessor.java (Thread-Safe Version)
import org.springframework.stereotype.Service;

@Service
public class PaymentProcessor {
    
    // State now passed via method parameters (Thread Stack)
    // No class-level mutable state
    
    public double processPayment(String transactionId, double amount, String userId) {
        // All variables are local to method - Thread Safe!
        double baseFee = amount * 0.025;
        double variableFee = calculateVariableFee(amount);
        double totalFee = baseFee + variableFee;
        double processedAmount = amount - totalFee;
        
        System.out.println("Thread " + Thread.currentThread().getId() + 
                          " processing: " + transactionId);
        System.out.println("  Transaction: " + transactionId);
        System.out.println("  User: " + userId);
        System.out.println("  Amount: $" + amount);
        System.out.println("  Total Fee: $" + totalFee);
        System.out.println("  Processed: $" + processedAmount);
        
        // Simulate processing
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        return processedAmount;
    }
    
    private double calculateVariableFee(double amount) {
        if (amount > 1000) {
            return amount * 0.015;
        } else if (amount > 500) {
            return amount * 0.01;
        } else {
            return amount * 0.005;
        }
    }
}

// PaymentController.java
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {
    
    private final PaymentProcessor processor;
    
    public PaymentController(PaymentProcessor processor) {
        this.processor = processor;
    }
    
    @PostMapping("/process")
    public String processPayment(@RequestParam String transactionId,
                                 @RequestParam double amount,
                                 @RequestParam String userId) {
        double result = processor.processPayment(transactionId, amount, userId);
        return "Payment processed: $" + result;
    }
}

// PaymentApplication.java
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class PaymentApplication {
    public static void main(String[] args) {
        ConfigurableApplicationContext context = 
            SpringApplication.run(PaymentApplication.class, args);
        
        PaymentProcessor processor = context.getBean(PaymentProcessor.class);
        
        System.out.println("=== Concurrent Payment Processing Demo ===\n");
        
        // Simulate concurrent requests
        Runnable task1 = () -> processor.processPayment("TXN-001", 150.50, "USER-A");
        Runnable task2 = () -> processor.processPayment("TXN-002", 2000.00, "USER-B");
        Runnable task3 = () -> processor.processPayment("TXN-003", 75.25, "USER-C");
        
        Thread t1 = new Thread(task1);
        Thread t2 = new Thread(task2);
        Thread t3 = new Thread(task3);
        
        t1.start();
        t2.start();
        t3.start();
        
        try {
            t1.join();
            t2.join();
            t3.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        System.out.println("\nAll payments processed successfully");
        context.close();
    }
}