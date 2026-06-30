// NotificationService.java
public interface NotificationService {
    void sendNotification(String message);
}

// EmailNotificationService.java
import org.springframework.stereotype.Service;

@Service("email")
public class EmailNotificationService implements NotificationService {
    @Override
    public void sendNotification(String message) {
        System.out.println("EMAIL: " + message);
    }
}

// SmsNotificationService.java
import org.springframework.stereotype.Service;

@Service("sms")
public class SmsNotificationService implements NotificationService {
    @Override
    public void sendNotification(String message) {
        System.out.println("SMS: " + message);
    }
}

// PushNotificationService.java
import org.springframework.stereotype.Service;

@Service("push")
public class PushNotificationService implements NotificationService {
    @Override
    public void sendNotification(String message) {
        System.out.println("PUSH: " + message);
    }
}

// TargetedNotificationSystem.java (Using @Qualifier)
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class TargetedNotificationSystem {
    
    private final NotificationService smsService;
    
    public TargetedNotificationSystem(@Qualifier("sms") NotificationService smsService) {
        this.smsService = smsService;
        System.out.println("TargetedNotificationSystem initialized with SMS service");
    }
    
    public void sendEmergencyAlert(String message) {
        smsService.sendNotification("🚨 EMERGENCY: " + message);
    }
}

// BroadCastNotificationSystem.java (Using List<T>)
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class BroadCastNotificationSystem {
    
    private final List<NotificationService> allServices;
    
    public BroadCastNotificationSystem(List<NotificationService> allServices) {
        this.allServices = allServices;
        System.out.println("BroadCastNotificationSystem initialized with " + 
                           allServices.size() + " services");
    }
    
    public void sendToAll(String message) {
        System.out.println("\n--- Broadcasting to all services ---");
        allServices.forEach(service -> 
            service.sendNotification(message)
        );
    }
}

// NotificationApplication.java
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class NotificationApplication {
    public static void main(String[] args) {
        ConfigurableApplicationContext context = 
            SpringApplication.run(NotificationApplication.class, args);
        
        System.out.println("\n=== Notification System Demo ===\n");
        
        // Using @Qualifier for targeted notifications
        TargetedNotificationSystem targeted = 
            context.getBean(TargetedNotificationSystem.class);
        targeted.sendEmergencyAlert("Server down in Production!");
        
        // Using List<T> for broadcast notifications
        BroadCastNotificationSystem broadcast = 
            context.getBean(BroadCastNotificationSystem.class);
        broadcast.sendToAll("Maintenance scheduled for 2am");
        
        context.close();
    }
}