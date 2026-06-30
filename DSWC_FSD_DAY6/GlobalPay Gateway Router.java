// PaymentProcessor.java
public interface PaymentProcessor {
    String processPayment(double amount);
}

// StripeProcessor.java
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Component
@Primary
public class StripeProcessor implements PaymentProcessor {
    @Override
    public String processPayment(double amount) {
        return "STRIPE: Processing $" + amount + " payment";
    }
}

// SquareProcessor.java
import org.springframework.stereotype.Component;

@Component
public class SquareProcessor implements PaymentProcessor {
    @Override
    public String processPayment(double amount) {
        return "SQUARE: Processing $" + amount + " payment";
    }
}

// BankXmlProcessor.java (Legacy - Cannot be modified)
public class BankXmlProcessor implements PaymentProcessor {
    private String apiKey;
    private String endpoint;
    private int timeout;
    private boolean secure;
    private String bankId;
    
    private BankXmlProcessor(Builder builder) {
        this.apiKey = builder.apiKey;
        this.endpoint = builder.endpoint;
        this.timeout = builder.timeout;
        this.secure = builder.secure;
        this.bankId = builder.bankId;
    }
    
    @Override
    public String processPayment(double amount) {
        return "BANK_XML: Processing $" + amount + " via legacy bank (ID: " + bankId + ")";
    }
    
    // Builder Pattern for complex instantiation
    public static class Builder {
        private String apiKey;
        private String endpoint;
        private int timeout = 30000;
        private boolean secure = true;
        private String bankId;
        
        public Builder apiKey(String apiKey) {
            this.apiKey = apiKey;
            return this;
        }
        
        public Builder endpoint(String endpoint) {
            this.endpoint = endpoint;
            return this;
        }
        
        public Builder timeout(int timeout) {
            this.timeout = timeout;
            return this;
        }
        
        public Builder secure(boolean secure) {
            this.secure = secure;
            return this;
        }
        
        public Builder bankId(String bankId) {
            this.bankId = bankId;
            return this;
        }
        
        public BankXmlProcessor build() {
            return new BankXmlProcessor(this);
        }
    }
}

// BankXmlProcessorFactoryBean.java
import org.springframework.beans.factory.FactoryBean;
import org.springframework.stereotype.Component;

@Component("bankXmlProcessor")
public class BankXmlProcessorFactoryBean implements FactoryBean<BankXmlProcessor> {
    
    @Override
    public BankXmlProcessor getObject() throws Exception {
        // Complex 5-step builder pattern instantiation
        return new BankXmlProcessor.Builder()
                .apiKey("LEGACY_BANK_API_KEY_12345")
                .endpoint("https://legacy-bank.example.com/xml-api")
                .timeout(60000)
                .secure(true)
                .bankId("BANK-LEGACY-001")
                .build();
    }
    
    @Override
    public Class<?> getObjectType() {
        return BankXmlProcessor.class;
    }
    
    @Override
    public boolean isSingleton() {
        return true; // Single instance is sufficient
    }
}

// CheckoutService.java
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class CheckoutService {
    
    private final PaymentProcessor defaultProcessor;
    private final PaymentProcessor legacyProcessor;
    
    public CheckoutService(PaymentProcessor defaultProcessor,
                           @Qualifier("bankXmlProcessor") PaymentProcessor legacyProcessor) {
        this.defaultProcessor = defaultProcessor;
        this.legacyProcessor = legacyProcessor;
        System.out.println("CheckoutService initialized");
        System.out.println("Default processor: " + defaultProcessor.getClass().getSimpleName());
        System.out.println("Legacy processor: " + legacyProcessor.getClass().getSimpleName());
    }
    
    public String checkoutDefault(double amount) {
        System.out.println("\n--- Using Default Processor ---");
        return defaultProcessor.processPayment(amount);
    }
    
    public String checkoutLegacy(double amount) {
        System.out.println("\n--- Using Legacy Processor ---");
        return legacyProcessor.processPayment(amount);
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
        
        CheckoutService service = context.getBean(CheckoutService.class);
        
        System.out.println("=== Payment Processing Demo ===\n");
        
        String result1 = service.checkoutDefault(150.50);
        System.out.println("Result: " + result1);
        
        String result2 = service.checkoutLegacy(250.75);
        System.out.println("Result: " + result2);
        
        context.close();
    }
}