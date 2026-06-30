// PIIProcessor.java (Marker Interface)
public interface PIIProcessor {
    // Marker interface - no methods required
}

// TransactionService.java
import org.springframework.stereotype.Service;

@Service
public class TransactionService implements PIIProcessor {
    
    public void processTransaction(String accountNumber, double amount) {
        System.out.println("Processing transaction for account: " + accountNumber);
        // PII processing logic
    }
}

// PublicCurrencyService.java
import org.springframework.stereotype.Service;

@Service
public class PublicCurrencyService {
    
    public double getExchangeRate(String fromCurrency, String toCurrency) {
        System.out.println("Getting exchange rate for " + fromCurrency + " to " + toCurrency);
        return 1.23;
    }
}

// PIIAuditProcessor.java
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

@Component
public class PIIAuditProcessor implements BeanPostProcessor {
    
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) 
            throws BeansException {
        // Return unmodified
        return bean;
    }
    
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) 
            throws BeansException {
        
        if (bean instanceof PIIProcessor) {
            System.out.println("=== COMPLIANCE AUDIT LOG ===");
            System.out.println("Bean Name: " + beanName);
            System.out.println("Bean Class: " + bean.getClass().getSimpleName());
            System.out.println("PII Processor: Securely initialized");
            System.out.println("Timestamp: " + System.currentTimeMillis());
            System.out.println("Status: COMPLIANT");
            System.out.println("================================");
        }
        
        return bean;
    }
}

// SecurityAuditApplication.java
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class SecurityAuditApplication {
    public static void main(String[] args) {
        System.out.println("=== Starting SecureBank Application ===");
        System.out.println("Security Audit Processor will intercept all beans\n");
        
        ConfigurableApplicationContext context = 
            SpringApplication.run(SecurityAuditApplication.class, args);
        
        System.out.println("\n=== Application Started ===");
        
        // Get beans to demonstrate they're created
        TransactionService transactionService = context.getBean(TransactionService.class);
        PublicCurrencyService currencyService = context.getBean(PublicCurrencyService.class);
        
        transactionService.processTransaction("123-456-789", 1000.00);
        currencyService.getExchangeRate("USD", "EUR");
        
        context.close();
    }
}