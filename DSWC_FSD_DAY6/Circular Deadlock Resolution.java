// InventoryService.java
import org.springframework.stereotype.Service;

@Service
public class InventoryService {
    private OrderService orderService;
    
    // Constructor Injection
    public InventoryService(OrderService orderService) {
        this.orderService = orderService;
        System.out.println("InventoryService initialized");
    }
    
    public void checkInventory(String productId) {
        System.out.println("Checking inventory for product: " + productId);
    }
    
    public void updateInventory(String productId, int quantity) {
        System.out.println("Updating inventory for " + productId + ": " + quantity);
        orderService.validateOrder(productId);
    }
}

// OrderService.java (Fixed with Setter Injection)
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderService {
    
    private InventoryService inventoryService;
    
    // Empty constructor for instantiation
    public OrderService() {
        System.out.println("OrderService created (Phase 1: Instantiation)");
    }
    
    // Setter Injection (Phase 2: Population)
    @Autowired
    public void setInventoryService(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
        System.out.println("InventoryService injected into OrderService (Phase 2)");
    }
    
    public void createOrder(String productId, int quantity) {
        System.out.println("Creating order for product: " + productId);
        inventoryService.checkInventory(productId);
        System.out.println("Order created successfully");
    }
    
    public void validateOrder(String productId) {
        System.out.println("Validating order for: " + productId);
    }
}

// CircularDependencyApplication.java
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class CircularDependencyApplication {
    public static void main(String[] args) {
        ConfigurableApplicationContext context = 
            SpringApplication.run(CircularDependencyApplication.class, args);
        
        System.out.println("\n=== Application Started Successfully ===");
        
        OrderService orderService = context.getBean(OrderService.class);
        InventoryService inventoryService = context.getBean(InventoryService.class);
        
        System.out.println("\n--- Testing Order Service ---");
        orderService.createOrder("PROD-001", 5);
        
        System.out.println("\n--- Testing Inventory Service ---");
        inventoryService.updateInventory("PROD-002", 10);
        
        context.close();
    }
}