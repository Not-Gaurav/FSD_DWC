// MediaAsset.java
import javax.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "asset_type", discriminatorType = DiscriminatorType.STRING)
public abstract class MediaAsset {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String title;
    private String description;
    private String uploadDate;
    private long fileSize;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id")
    private Author author;
    
    public MediaAsset() {}
    
    public MediaAsset(String title, String description, String uploadDate, long fileSize) {
        this.title = title;
        this.description = description;
        this.uploadDate = uploadDate;
        this.fileSize = fileSize;
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getUploadDate() { return uploadDate; }
    public void setUploadDate(String uploadDate) { this.uploadDate = uploadDate; }
    public long getFileSize() { return fileSize; }
    public void setFileSize(long fileSize) { this.fileSize = fileSize; }
    public Author getAuthor() { return author; }
    public void setAuthor(Author author) { this.author = author; }
}

// ImageAsset.java
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("IMAGE")
public class ImageAsset extends MediaAsset {
    
    private String resolution;
    private String format;
    private String colorProfile;
    
    public ImageAsset() {}
    
    public ImageAsset(String title, String description, String uploadDate, long fileSize,
                     String resolution, String format, String colorProfile) {
        super(title, description, uploadDate, fileSize);
        this.resolution = resolution;
        this.format = format;
        this.colorProfile = colorProfile;
    }
    
    public String getResolution() { return resolution; }
    public void setResolution(String resolution) { this.resolution = resolution; }
    public String getFormat() { return format; }
    public void setFormat(String format) { this.format = format; }
    public String getColorProfile() { return colorProfile; }
    public void setColorProfile(String colorProfile) { this.colorProfile = colorProfile; }
}

// VideoAsset.java
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("VIDEO")
public class VideoAsset extends MediaAsset {
    
    private String resolution; // e.g., "1920x1080"
    private String duration;
    private String codec;
    private int bitrate;
    
    public VideoAsset() {}
    
    public VideoAsset(String title, String description, String uploadDate, long fileSize,
                     String resolution, String duration, String codec, int bitrate) {
        super(title, description, uploadDate, fileSize);
        this.resolution = resolution;
        this.duration = duration;
        this.codec = codec;
        this.bitrate = bitrate;
    }
    
    public String getResolution() { return resolution; }
    public void setResolution(String resolution) { this.resolution = resolution; }
    public String getDuration() { return duration; }
    public void setDuration(String duration) { this.duration = duration; }
    public String getCodec() { return codec; }
    public void setCodec(String codec) { this.codec = codec; }
    public int getBitrate() { return bitrate; }
    public void setBitrate(int bitrate) { this.bitrate = bitrate; }
}

// Author.java
import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Author {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String username;
    private String email;
    private String fullName;
    private String bio;
    
    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<MediaAsset> mediaAssets = new ArrayList<>();
    
    public Author() {}
    
    public Author(String username, String email, String fullName, String bio) {
        this.username = username;
        this.email = email;
        this.fullName = fullName;
        this.bio = bio;
    }
    
    public void addMediaAsset(MediaAsset asset) {
        mediaAssets.add(asset);
        asset.setAuthor(this);
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public String getBio() { return bio; }
    public void setBio(String bio) { this.bio = bio; }
    public List<MediaAsset> getMediaAssets() { return mediaAssets; }
    public void setMediaAssets(List<MediaAsset> mediaAssets) { this.mediaAssets = mediaAssets; }
}

// AuthorRepository.java
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {
    
    // Using TREAT operator for polymorphic downcasting
    @Query("SELECT ma FROM MediaAsset ma " +
           "WHERE ma.author.id = :authorId " +
           "AND TREAT(ma AS VideoAsset).resolution LIKE CONCAT('%', :resolution, '%')")
    List<MediaAsset> findVideosByResolution(@Param("authorId") Long authorId, 
                                            @Param("resolution") String resolution);
    
    // @EntityGraph for eager fetching
    @EntityGraph(attributePaths = {"mediaAssets"})
    Optional<Author> findByUsername(String username);
}