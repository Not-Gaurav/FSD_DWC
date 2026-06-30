// Auditable.java (Marker Interface)
public interface Auditable {
    void auditRegister();
}

// DataAccessService.java
import org.springframework.stereotype.Service;

@Service
public class DataAccessService implements Auditable {
    
    @Override
    public void auditRegister() {
        System.out.println("DataAccessService registered in SecurityRegistry");
    }
    
    public void accessData(String dataId) {
        System.out.println("Accessing data: " + dataId);
    }
}

// TransactionLogService.java
import org.springframework.stereotype.Service;

@Service
public class TransactionLogService implements Auditable {
    
    @Override
    public void auditRegister() {
        System.out.println("TransactionLogService registered in SecurityRegistry");
    }
    
    public void logTransaction(String transactionId) {
        System.out.println("Logging transaction: " + transactionId);
    }
}

// NonAuditableService.java
import org.springframework.stereotype.Service;

@Service
public class NonAuditableService {
    
    public void doSomething() {
        System.out.println("Doing something non-auditable");
    }
}

// SecurityInterceptor.java
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

@Component
public class SecurityInterceptor implements BeanPostProcessor {
    
    private int auditableCount = 0;
    
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) 
            throws BeansException {
        return bean;
    }
    
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) 
            throws BeansException {
        
        if (bean instanceof Auditable) {
            auditableCount++;
            Auditable auditable = (Auditable) bean;
            
            System.out.println("\n=== SECURITY INTERCEPTOR ===");
            System.out.println("Bean Name: " + beanName);
            System.out.println("Bean Type: " + bean.getClass().getSimpleName());
            System.out.println("Registering in SecurityRegistry...");
            
            auditable.auditRegister();
            
            System.out.println("Auditable count: " + auditableCount);
            System.out.println("================================");
        }
        
        return bean;
    }
}

// SecurityApplication.java
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class SecurityApplication {
    public static void main(String[] args) {
        System.out.println("=== Starting Security Application ===");
        System.out.println("SecurityInterceptor will audit all Auditable beans\n");
        
        ConfigurableApplicationContext context = 
            SpringApplication.run(SecurityApplication.class, args);
        
        System.out.println("\n=== Application Ready ===");
        
        // Get beans to verify they work
        DataAccessService dataService = context.getBean(DataAccessService.class);
        TransactionLogService logService = context.getBean(TransactionLogService.class);
        NonAuditableService nonAuditable = context.getBean(NonAuditableService.class);
        
        dataService.accessData("USER-123");
        logService.logTransaction("TXN-456");
        nonAuditable.doSomething();
        
        context.close();
    }
}