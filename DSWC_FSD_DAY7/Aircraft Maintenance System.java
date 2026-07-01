// MaintenanceLog.java (Embeddable)
import javax.persistence.Embeddable;

@Embeddable
public class MaintenanceLog {
    
    private String maintenanceDate;
    private String technicianName;
    private String description;
    private String maintenanceType; // ROUTINE, REPAIR, INSPECTION
    private int durationHours;
    private boolean completed;
    
    public MaintenanceLog() {}
    
    public MaintenanceLog(String maintenanceDate, String technicianName, 
                         String description, String maintenanceType, 
                         int durationHours, boolean completed) {
        this.maintenanceDate = maintenanceDate;
        this.technicianName = technicianName;
        this.description = description;
        this.maintenanceType = maintenanceType;
        this.durationHours = durationHours;
        this.completed = completed;
    }
    
    public String getMaintenanceDate() { return maintenanceDate; }
    public void setMaintenanceDate(String maintenanceDate) { this.maintenanceDate = maintenanceDate; }
    public String getTechnicianName() { return technicianName; }
    public void setTechnicianName(String technicianName) { this.technicianName = technicianName; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getMaintenanceType() { return maintenanceType; }
    public void setMaintenanceType(String maintenanceType) { this.maintenanceType = maintenanceType; }
    public int getDurationHours() { return durationHours; }
    public void setDurationHours(int durationHours) { this.durationHours = durationHours; }
    public boolean isCompleted() { return completed; }
    public void setCompleted(boolean completed) { this.completed = completed; }
}

// Aircraft.java
import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Aircraft {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String tailNumber;
    private String modelName;
    private String manufacturer;
    private int yearManufactured;
    private int totalFlightHours;
    private boolean isGrounded;
    
    @ElementCollection
    @CollectionTable(name = "maintenance_logs", joinColumns = @JoinColumn(name = "aircraft_id"))
    private List<MaintenanceLog> maintenanceLogs = new ArrayList<>();
    
    public Aircraft() {}
    
    public Aircraft(String tailNumber, String modelName, String manufacturer, 
                   int yearManufactured, int totalFlightHours, boolean isGrounded) {
        this.tailNumber = tailNumber;
        this.modelName = modelName;
        this.manufacturer = manufacturer;
        this.yearManufactured = yearManufactured;
        this.totalFlightHours = totalFlightHours;
        this.isGrounded = isGrounded;
    }
    
    public void addMaintenanceLog(MaintenanceLog log) {
        maintenanceLogs.add(log);
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTailNumber() { return tailNumber; }
    public void setTailNumber(String tailNumber) { this.tailNumber = tailNumber; }
    public String getModelName() { return modelName; }
    public void setModelName(String modelName) { this.modelName = modelName; }
    public String getManufacturer() { return manufacturer; }
    public void setManufacturer(String manufacturer) { this.manufacturer = manufacturer; }
    public int getYearManufactured() { return yearManufactured; }
    public void setYearManufactured(int yearManufactured) { this.yearManufactured = yearManufactured; }
    public int getTotalFlightHours() { return totalFlightHours; }
    public void setTotalFlightHours(int totalFlightHours) { this.totalFlightHours = totalFlightHours; }
    public boolean isGrounded() { return isGrounded; }
    public void setGrounded(boolean grounded) { isGrounded = grounded; }
    public List<MaintenanceLog> getMaintenanceLogs() { return maintenanceLogs; }
    public void setMaintenanceLogs(List<MaintenanceLog> maintenanceLogs) { this.maintenanceLogs = maintenanceLogs; }
}

// AircraftRepository.java
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AircraftRepository extends JpaRepository<Aircraft, Long> {
    
    // Paginated JPQL query with @ElementCollection search
    @Query("SELECT a FROM Aircraft a " +
           "JOIN a.maintenanceLogs ml " +
           "WHERE ml.maintenanceType = :type " +
           "AND ml.completed = false")
    Page<Aircraft> findAircraftWithPendingMaintenance(@Param("type") String type, Pageable pageable);
    
    // Derived query method with multiple conditions
    List<Aircraft> findByModelNameInAndIsGroundedTrue(List<String> modelNames);
}