// WebhookIntegration.java
public interface WebhookIntegration {
    void sendEvent(String eventType, String payload);
}

// SlackIntegration.java
import org.springframework.stereotype.Component;

@Component
public class SlackIntegration implements WebhookIntegration {
    @Override
    public void sendEvent(String eventType, String payload) {
        System.out.println("SLACK: Sending " + eventType + " event: " + payload);
    }
}

// DiscordIntegration.java
import org.springframework.stereotype.Component;

@Component
public class DiscordIntegration implements WebhookIntegration {
    @Override
    public void sendEvent(String eventType, String payload) {
        System.out.println("DISCORD: Sending " + eventType + " event: " + payload);
    }
}

// EmailIntegration.java
import org.springframework.stereotype.Component;

@Component
public class EmailIntegration implements WebhookIntegration {
    @Override
    public void sendEvent(String eventType, String payload) {
        System.out.println("EMAIL: Sending " + eventType + " event: " + payload);
    }
}

// WebhookDispatcher.java
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.SmartLifecycle;
import org.springframework.context.smart.SmartInitializingSingleton;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class WebhookDispatcher implements ApplicationContextAware, SmartInitializingSingleton {
    
    private ApplicationContext applicationContext;
    private Map<String, WebhookIntegration> routingTable;
    private boolean ready = false;
    
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) 
            throws BeansException {
        this.applicationContext = applicationContext;
        System.out.println("ApplicationContext set for WebhookDispatcher");
    }
    
    @Override
    public void afterSingletonsInstantiated() {
        System.out.println("\n=== SmartInitializingSingleton: Building Routing Table ===");
        System.out.println("All singletons have been instantiated");
        System.out.println("Now dynamically discovering WebhookIntegration beans...");
        
        // Get all WebhookIntegration beans
        routingTable = applicationContext.getBeansOfType(WebhookIntegration.class);
        
        System.out.println("Discovered " + routingTable.size() + " integrations:");
        routingTable.forEach((name, integration) -> 
            System.out.println("  - " + name + " (" + integration.getClass().getSimpleName() + ")")
        );
        
        ready = true;
        System.out.println("Routing table built successfully!");
        System.out.println("WebhookDispatcher is ready to dispatch events");
    }
    
    public void dispatchEvent(String eventType, String payload) {
        if (!ready) {
            System.out.println("Dispatcher not ready! Routing table not built");
            return;
        }
        
        System.out.println("\n=== Dispatching Event ===");
        System.out.println("Event Type: " + eventType);
        System.out.println("Payload: " + payload);
        
        routingTable.values().forEach(integration -> 
            integration.sendEvent(eventType, payload)
        );
    }
}

// WebhookApplication.java
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class WebhookApplication {
    public static void main(String[] args) {
        System.out.println("=== Starting StreamHub Application ===");
        System.out.println("WebhookDispatcher will build routing table after all beans are ready\n");
        
        ConfigurableApplicationContext context = 
            SpringApplication.run(WebhookApplication.class, args);
        
        WebhookDispatcher dispatcher = context.getBean(WebhookDispatcher.class);
        
        // Dispatch some events
        dispatcher.dispatchEvent("USER_CREATED", "{\"userId\": 123, \"name\": \"John Doe\"}");
        dispatcher.dispatchEvent("PAYMENT_SUCCESS", "{\"amount\": 150.50, \"status\": \"completed\"}");
        
        context.close();
    }
}