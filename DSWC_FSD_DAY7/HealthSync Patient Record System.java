// MedicalRecord.java (Base Entity)
import javax.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "record_type", discriminatorType = DiscriminatorType.STRING)
public abstract class MedicalRecord {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String recordDate;
    private String description;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id")
    private Patient patient;
    
    // Constructors, getters, setters
    public MedicalRecord() {}
    
    public MedicalRecord(String recordDate, String description) {
        this.recordDate = recordDate;
        this.description = description;
    }
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getRecordDate() { return recordDate; }
    public void setRecordDate(String recordDate) { this.recordDate = recordDate; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public Patient getPatient() { return patient; }
    public void setPatient(Patient patient) { this.patient = patient; }
}

// PrescriptionRecord.java
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("PRESCRIPTION")
public class PrescriptionRecord extends MedicalRecord {
    
    private String medicationName;
    private String dosage;
    private int quantity;
    private String prescribingDoctor;
    
    public PrescriptionRecord() {}
    
    public PrescriptionRecord(String recordDate, String description, String medicationName, 
                             String dosage, int quantity, String prescribingDoctor) {
        super(recordDate, description);
        this.medicationName = medicationName;
        this.dosage = dosage;
        this.quantity = quantity;
        this.prescribingDoctor = prescribingDoctor;
    }
    
    public String getMedicationName() { return medicationName; }
    public void setMedicationName(String medicationName) { this.medicationName = medicationName; }
    public String getDosage() { return dosage; }
    public void setDosage(String dosage) { this.dosage = dosage; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public String getPrescribingDoctor() { return prescribingDoctor; }
    public void setPrescribingDoctor(String prescribingDoctor) { this.prescribingDoctor = prescribingDoctor; }
}

// LabResultRecord.java
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("LAB_RESULT")
public class LabResultRecord extends MedicalRecord {
    
    private String labTestName;
    private String resultValue;
    private String referenceRange;
    private String labTechnician;
    
    public LabResultRecord() {}
    
    public LabResultRecord(String recordDate, String description, String labTestName, 
                          String resultValue, String referenceRange, String labTechnician) {
        super(recordDate, description);
        this.labTestName = labTestName;
        this.resultValue = resultValue;
        this.referenceRange = referenceRange;
        this.labTechnician = labTechnician;
    }
    
    public String getLabTestName() { return labTestName; }
    public void setLabTestName(String labTestName) { this.labTestName = labTestName; }
    public String getResultValue() { return resultValue; }
    public void setResultValue(String resultValue) { this.resultValue = resultValue; }
    public String getReferenceRange() { return referenceRange; }
    public void setReferenceRange(String referenceRange) { this.referenceRange = referenceRange; }
    public String getLabTechnician() { return labTechnician; }
    public void setLabTechnician(String labTechnician) { this.labTechnician = labTechnician; }
}

// Patient.java
import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Patient {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String patientName;
    private String dateOfBirth;
    private String email;
    private String phoneNumber;
    
    // One-to-Many Bidirectional - Owning Side
    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MedicalRecord> medicalRecords = new ArrayList<>();
    
    // One-to-One with @MapsId
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "id")
    private BillingAccount billingAccount;
    
    public Patient() {}
    
    public Patient(String patientName, String dateOfBirth, String email, String phoneNumber) {
        this.patientName = patientName;
        this.dateOfBirth = dateOfBirth;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }
    
    public void addMedicalRecord(MedicalRecord record) {
        medicalRecords.add(record);
        record.setPatient(this);
    }
    
    public void removeMedicalRecord(MedicalRecord record) {
        medicalRecords.remove(record);
        record.setPatient(null);
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getPatientName() { return patientName; }
    public void setPatientName(String patientName) { this.patientName = patientName; }
    public String getDateOfBirth() { return dateOfBirth; }
    public void setDateOfBirth(String dateOfBirth) { this.dateOfBirth = dateOfBirth; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    public List<MedicalRecord> getMedicalRecords() { return medicalRecords; }
    public void setMedicalRecords(List<MedicalRecord> medicalRecords) { this.medicalRecords = medicalRecords; }
    public BillingAccount getBillingAccount() { return billingAccount; }
    public void setBillingAccount(BillingAccount billingAccount) { 
        this.billingAccount = billingAccount;
        billingAccount.setPatient(this);
    }
}

// BillingAccount.java
import javax.persistence.*;
import java.math.BigDecimal;

@Entity
public class BillingAccount {
    
    @Id
    private Long id; // Same as Patient ID
    
    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "id")
    private Patient patient;
    
    private BigDecimal currentBalance;
    private String insuranceProvider;
    private String billingAddress;
    
    public BillingAccount() {}
    
    public BillingAccount(BigDecimal currentBalance, String insuranceProvider, String billingAddress) {
        this.currentBalance = currentBalance;
        this.insuranceProvider = insuranceProvider;
        this.billingAddress = billingAddress;
    }
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Patient getPatient() { return patient; }
    public void setPatient(Patient patient) { this.patient = patient; }
    public BigDecimal getCurrentBalance() { return currentBalance; }
    public void setCurrentBalance(BigDecimal currentBalance) { this.currentBalance = currentBalance; }
    public String getInsuranceProvider() { return insuranceProvider; }
    public void setInsuranceProvider(String insuranceProvider) { this.insuranceProvider = insuranceProvider; }
    public String getBillingAddress() { return billingAddress; }
    public void setBillingAddress(String billingAddress) { this.billingAddress = billingAddress; }
}

// PatientBillingSummaryDTO.java
import java.math.BigDecimal;

public class PatientBillingSummaryDTO {
    private String patientName;
    private String recordType;
    private BigDecimal currentBalance;
    
    public PatientBillingSummaryDTO(String patientName, String recordType, BigDecimal currentBalance) {
        this.patientName = patientName;
        this.recordType = recordType;
        this.currentBalance = currentBalance;
    }
    
    public String getPatientName() { return patientName; }
    public String getRecordType() { return recordType; }
    public BigDecimal getCurrentBalance() { return currentBalance; }
    
    @Override
    public String toString() {
        return "PatientBillingSummaryDTO{" +
                "patientName='" + patientName + '\'' +
                ", recordType='" + recordType + '\'' +
                ", currentBalance=" + currentBalance +
                '}';
    }
}

// PatientRepository.java
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.math.BigDecimal;
import java.util.List;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {
    
    // JPQL with Constructor Expression
    @Query("SELECT new com.healthsync.dto.PatientBillingSummaryDTO(" +
           "p.patientName, " +
           "CASE TYPE(mr) " +
           "   WHEN PrescriptionRecord THEN 'PRESCRIPTION' " +
           "   WHEN LabResultRecord THEN 'LAB_RESULT' " +
           "   ELSE 'UNKNOWN' " +
           "END, " +
           "b.currentBalance) " +
           "FROM Patient p " +
           "JOIN p.medicalRecords mr " +
           "JOIN p.billingAccount b " +
           "WHERE b.currentBalance IS NOT NULL")
    List<PatientBillingSummaryDTO> findPatientBillingSummary();
    
    // Derived Query Method with Property Traversal
    List<Patient> findByBillingAccount_CurrentBalanceGreaterThanAndMedicalRecords_MedicationName(
        BigDecimal balance, String medicationName);
}