package cl.dssm.rrff.domain;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.UUID;
@MappedSuperclass
public abstract class BaseEntity {
  @Id @Column(name="id",length=36,nullable=false,updatable=false) private String id;
  @Version @Column(name="row_version",nullable=false) private Long rowVersion;
  @Column(name="created_at",nullable=false,updatable=false) private LocalDateTime createdAt;
  @Column(name="updated_at",nullable=false) private LocalDateTime updatedAt;
  @PrePersist protected void insert(){if(id==null)id=UUID.randomUUID().toString();createdAt=LocalDateTime.now(ZoneOffset.UTC);updatedAt=createdAt;}
  @PreUpdate protected void update(){updatedAt=LocalDateTime.now(ZoneOffset.UTC);}
  public String getId(){return id;}
  public Long getRowVersion(){return rowVersion;}
  public LocalDateTime getCreatedAt(){return createdAt;}
  public LocalDateTime getUpdatedAt(){return updatedAt;}
}
