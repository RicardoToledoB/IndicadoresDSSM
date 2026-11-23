package cl.dssm.rrff.domain;
import jakarta.persistence.*;
@Entity @Table(name="usuario")
public class Usuario extends BaseEntity {
  @Column(name="username",length=100,nullable=false) private String username;
  @Column(name="password_hash",length=255,nullable=false) private String passwordHash;
  @Column(name="activo",nullable=false) private Boolean activo;
  public String getUsername(){return username;}
  public void setUsername(String value){this.username=value;}
  public String getPasswordHash(){return passwordHash;}
  public void setPasswordHash(String value){this.passwordHash=value;}
  public Boolean getActivo(){return activo;}
  public void setActivo(Boolean value){this.activo=value;}
  @ElementCollection(fetch=FetchType.EAGER)
  @CollectionTable(name="usuario_rol", joinColumns=@JoinColumn(name="usuario_id"))
  @Column(name="rol",nullable=false,length=80)
  private java.util.Set<String> roles=new java.util.HashSet<>();
  public java.util.Set<String> getRoles(){return roles;}
  public void setRoles(java.util.Set<String> roles){this.roles=roles;}
}
