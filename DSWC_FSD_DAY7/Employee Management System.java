// Employee.java
import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Employee {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String firstName;
    private String lastName;
    private String email;
    private String department;
    private String position;
    private double salary;
    
    // Self-Referencing Relationship
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "manager_id")
    private Employee manager;
    
    @OneToMany(mappedBy = "manager", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Employee> directReports = new ArrayList<>();
    
    public Employee() {}
    
    public Employee(String firstName, String lastName, String email, 
                   String department, String position, double salary) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.department = department;
        this.position = position;
        this.salary = salary;
    }
    
    public void addDirectReport(Employee employee) {
        directReports.add(employee);
        employee.setManager(this);
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }
    public String getPosition() { return position; }
    public void setPosition(String position) { this.position = position; }
    public double getSalary() { return salary; }
    public void setSalary(double salary) { this.salary = salary; }
    public Employee getManager() { return manager; }
    public void setManager(Employee manager) { this.manager = manager; }
    public List<Employee> getDirectReports() { return directReports; }
    public void setDirectReports(List<Employee> directReports) { this.directReports = directReports; }
}

// FullTimeEmployee.java
import javax.persistence.Entity;

@Entity
public class FullTimeEmployee extends Employee {
    
    private double annualBonus;
    private int vacationDays;
    private String benefitsPackage;
    
    public FullTimeEmployee() {}
    
    public FullTimeEmployee(String firstName, String lastName, String email, 
                           String department, String position, double salary,
                           double annualBonus, int vacationDays, String benefitsPackage) {
        super(firstName, lastName, email, department, position, salary);
        this.annualBonus = annualBonus;
        this.vacationDays = vacationDays;
        this.benefitsPackage = benefitsPackage;
    }
    
    public double getAnnualBonus() { return annualBonus; }
    public void setAnnualBonus(double annualBonus) { this.annualBonus = annualBonus; }
    public int getVacationDays() { return vacationDays; }
    public void setVacationDays(int vacationDays) { this.vacationDays = vacationDays; }
    public String getBenefitsPackage() { return benefitsPackage; }
    public void setBenefitsPackage(String benefitsPackage) { this.benefitsPackage = benefitsPackage; }
}

// Contractor.java
import javax.persistence.Entity;

@Entity
public class Contractor extends Employee {
    
    private double hourlyRate;
    private String contractEndDate;
    private String companyName;
    
    public Contractor() {}
    
    public Contractor(String firstName, String lastName, String email, 
                     String department, String position, double salary,
                     double hourlyRate, String contractEndDate, String companyName) {
        super(firstName, lastName, email, department, position, salary);
        this.hourlyRate = hourlyRate;
        this.contractEndDate = contractEndDate;
        this.companyName = companyName;
    }
    
    public double getHourlyRate() { return hourlyRate; }
    public void setHourlyRate(double hourlyRate) { this.hourlyRate = hourlyRate; }
    public String getContractEndDate() { return contractEndDate; }
    public void setContractEndDate(String contractEndDate) { this.contractEndDate = contractEndDate; }
    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }
}

// ManagerSpanDTO.java
public class ManagerSpanDTO {
    private String managerName;
    private Long directReportCount;
    
    public ManagerSpanDTO(String managerName, Long directReportCount) {
        this.managerName = managerName;
        this.directReportCount = directReportCount;
    }
    
    public String getManagerName() { return managerName; }
    public Long getDirectReportCount() { return directReportCount; }
    
    @Override
    public String toString() {
        return "ManagerSpanDTO{" +
                "managerName='" + managerName + '\'' +
                ", directReportCount=" + directReportCount +
                '}';
    }
}

// EmployeeRepository.java
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    
    // JPQL with GROUP BY and COUNT
    @Query("SELECT new com.employee.dto.ManagerSpanDTO(" +
           "CONCAT(m.firstName, ' ', m.lastName), " +
           "COUNT(e)) " +
           "FROM Employee e " +
           "INNER JOIN e.manager m " +
           "GROUP BY m.id, m.firstName, m.lastName")
    List<ManagerSpanDTO> findManagerSpanReport();
    
    // Derived Query Method with manager name and department
    List<Employee> findByManager_FirstNameAndDepartment(String firstName, String department);
}