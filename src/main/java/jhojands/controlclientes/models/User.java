/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jhojands.controlclientes.models;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

/**
 * @author JHOJAN L
 */
@Entity
@Table(name = "users")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;

    @Basic(optional = false)
    @NotNull(message = "no puede ser nulo")
    @Positive(message = "debe ser valor positivo")
    @Size(min = 8, max = 10, message = " debe tener minimo 8 digitos y maximo 10 digitos")
    @Column(unique = true)
    private String identification;

    @Basic(optional = false)
    @NotNull(message = "no puede ser nulo")
    @Size(min = 4, max = 30, message = " debe tener mínimo 4 caracteres y máximo 30")
    private String name;

    @Column(name = "last_name")
    @Basic(optional = false)
    @NotNull(message = "no puede ser nulo")
    @Size(min = 4, max = 30, message = " debe tener mínimo 4 caracteres y máximo 30")
    private String lastName;

    // @Pattern(regexp="[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", message="Invalid email")//if the field contains email address consider using this annotation to enforce field validation
    @Basic(optional = true)
    @Size(max = 30, message = "debe tener máximo 30 caracteres")
    @Email(message = "proporcionado no coincide con un correo electronico")
    @Column(nullable = true, unique = true)
    private String email;

    @Basic(optional = true)
    @Size(min = 5, max = 256, message = "debe tener mínimo 8 caracteres")
    @Column(nullable = true)
    private String password;

    @Size(min = 1, max = 100, message = "debe tener máximo 100 caracteres")
    @Column(nullable = true)
    private String address;

    @Column(name = "create_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createAt;

    @NotNull(message = "no puede ser nulo")
    private boolean enabled;

    @Basic(optional = true)
    @Column(nullable = true)
    private long balance;

    @NotNull(message = "no puede ser nulo")
    @JoinColumn(name = "role_id", referencedColumnName = "id")
    @ManyToOne(cascade = {CascadeType.MERGE})
    private Role idRole;

    @PrePersist
    public void prePersist() {//al crearse un cliente se registra la fecha de la misma
        this.createAt = new Date();
    }

    public User() {
    }

    public User(String identification, String name, String lastName, String email, String password, String address, Date createAt, boolean enabled, long balance, Role idRole) {
        this.identification = identification;
        this.name = name;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.address = address;
        this.createAt = createAt;
        this.enabled = enabled;
        this.balance = balance;
        this.idRole = idRole;
    }

    public User(Long id, String identification, String name, String lastName, String email, String password, String address, Date createAt, boolean enabled, long balance, Role idRole) {
        this.id = id;
        this.identification = identification;
        this.name = name;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.address = address;
        this.createAt = createAt;
        this.enabled = enabled;
        this.balance = balance;
        this.idRole = idRole;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIdentification() {
        return identification;
    }

    public void setIdentification(String identification) {
        this.identification = identification;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public long getBalance() {
        return balance;
    }

    public void setBalance(long balance) {
        this.balance = balance;
    }

    public Role getIdRole() {
        return idRole;
    }

    public void setIdRole(Role idRole) {
        this.idRole = idRole;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof User)) {
            return false;
        }
        User other = (User) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Usuario{" + "id=" + id + ", identification=" + identification + ", name=" + name + ", lastName=" + lastName + ", email=" + email + ", password=" + password + ", address=" + address + ", createAt=" + createAt + ", enabled=" + enabled + ", balance=" + balance + ", idRol=" + idRole + '}';
    }

}
