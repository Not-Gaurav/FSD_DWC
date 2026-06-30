// TradingStrategy.java
public interface TradingStrategy {
    void executeTrade();
}

// AbstractStrategy.java
public abstract class AbstractStrategy implements TradingStrategy {
    protected String assetClass;
    
    public AbstractStrategy(String assetClass) {
        this.assetClass = assetClass;
    }
    
    public String getAssetClass() {
        return assetClass;
    }
}

// MomentumStrategy.java
import org.springframework.stereotype.Component;

@Component
public class MomentumStrategy extends AbstractStrategy {
    
    public MomentumStrategy() {
        super("Equity");
    }
    
    @Override
    public void executeTrade() {
        System.out.println("Executing Momentum Strategy for " + assetClass);
        // Momentum trading logic
    }
}

// ArbitrageStrategy.java
import org.springframework.stereotype.Component;

@Component
public class ArbitrageStrategy extends AbstractStrategy {
    
    public ArbitrageStrategy() {
        super("Forex");
    }
    
    @Override
    public void executeTrade() {
        System.out.println("Executing Arbitrage Strategy for " + assetClass);
        // Arbitrage trading logic
    }
}

// MarketDataService.java
import org.springframework.stereotype.Service;

@Service
public class MarketDataService {
    private boolean cacheWarmedUp = false;
    
    public void warmUpCache() {
        System.out.println("Warming up market data cache...");
        // Simulate cache warming
        cacheWarmedUp = true;
        System.out.println("Market data cache warmed up successfully");
    }
    
    public boolean isCacheWarmedUp() {
        return cacheWarmedUp;
    }
}

// AlertService.java
import org.springframework.stereotype.Service;

@Service
public class AlertService {
    public void sendAlert(String message) {
        System.out.println("ALERT: " + message);
    }
}

// TradingEngine.java
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.List;

@Component
public class TradingEngine implements BeanNameAware, InitializingBean {
    
    private final MarketDataService marketDataService;
    private final List<TradingStrategy> strategies;
    private AlertService alertService;
    private String beanName;
    private boolean isInitialized = false;
    
    // Constructor Injection (Mandatory Dependencies)
    public TradingEngine(MarketDataService marketDataService, 
                         List<TradingStrategy> strategies) {
        this.marketDataService = marketDataService;
        this.strategies = strategies;
        System.out.println("TradingEngine constructor: Dependencies injected");
        System.out.println("Found " + strategies.size() + " trading strategies");
    }
    
    // Setter Injection (Optional Dependency)
    @Autowired(required = false)
    public void setAlertService(AlertService alertService) {
        this.alertService = alertService;
        if (alertService != null) {
            System.out.println("AlertService injected successfully");
        } else {
            System.out.println("AlertService not available (optional dependency)");
        }
    }
    
    // BeanNameAware implementation
    @Override
    public void setBeanName(String name) {
        this.beanName = name;
        System.out.println("BeanNameAware: Bean name set to '" + name + "'");
    }
    
    // JSR-250 Initialization
    @PostConstruct
    public void warmUpCache() {
        System.out.println("@PostConstruct: Warming up cache...");
        marketDataService.warmUpCache();
        System.out.println("@PostConstruct: Cache warm-up complete");
    }
    
    // Spring-specific Initialization
    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("InitializingBean: Performing safety validation...");
        
        // Validate market data service
        if (marketDataService == null) {
            throw new IllegalStateException("MarketDataService is null!");
        }
        
        // Validate strategies are loaded
        if (strategies == null || strategies.isEmpty()) {
            throw new IllegalStateException("No trading strategies loaded!");
        }
        
        // Validate cache warm-up
        if (!marketDataService.isCacheWarmedUp()) {
            throw new IllegalStateException("Cache not warmed up properly!");
        }
        
        isInitialized = true;
        System.out.println("InitializingBean: Safety validation passed");
        System.out.println("TradingEngine ready for trading operations");
    }
    
    // JSR-250 Destruction
    @PreDestroy
    public void closeOpenPositions() {
        System.out.println("@PreDestroy: Closing all open market positions...");
        // Simulate closing positions
        System.out.println("All open positions closed successfully");
    }
    
    public void startTrading() {
        if (!isInitialized) {
            throw new IllegalStateException("Engine not initialized!");
        }
        
        System.out.println("\n=== Starting Trading Operations ===");
        System.out.println("Bean Name: " + beanName);
        System.out.println("Strategies: " + strategies.size());
        System.out.println("Alert Service: " + (alertService != null ? "Available" : "Not Available"));
        
        for (TradingStrategy strategy : strategies) {
            strategy.executeTrade();
            if (alertService != null) {
                alertService.sendAlert("Trade executed using " + 
                    strategy.getClass().getSimpleName());
            }
        }
    }
}

// TradingApplication.java
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class TradingApplication {
    public static void main(String[] args) {
        ConfigurableApplicationContext context = 
            SpringApplication.run(TradingApplication.class, args);
        
        TradingEngine engine = context.getBean(TradingEngine.class);
        engine.startTrading();
        
        // Simulate shutdown
        System.out.println("\n=== Shutting down application ===");
        context.close();
    }
}