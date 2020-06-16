package fr.ptcherniati.pg_policies.model.security;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import java.util.Objects;
import java.util.Optional;

@Entity
@NamedQuery(name = "Authorities.findByUsername", query = "select a from Authorities a where a.authoritiesID.username = ?1")
@JsonIgnoreProperties(value = {"username", "authorities"}, allowSetters = true, allowGetters = false)
public class Authorities {
    @Embedded
    @Id
    AuthoritiesID authoritiesID = new AuthoritiesID();

    public Authorities() {
    }

    public Authorities(String username, Roles role) {
        this.authoritiesID.setUsername(username);
        this.authoritiesID.setRoles(role);
    }

    public String getUsername() {
        return Optional.ofNullable(authoritiesID).map(AuthoritiesID::getUsername).orElse(null);
    }

    public void setUsername(String username) {
        this.authoritiesID.setUsername(username);
    }

    public String getAuthorities() {
        return Optional.ofNullable(authoritiesID).map(authoritiesID1 -> authoritiesID1.getRoles().getNom()
        ).orElse(null);
    }

    public Roles getRoles() {
        return Optional.ofNullable(authoritiesID).map(authoritiesID1 -> authoritiesID1.getRoles()
        ).orElse(null);
    }

    public void setRoles(Roles roles) {
        this.authoritiesID.setRoles(roles);
    }

    @Override
    public String toString() {
        return "Authorities{" +
                "username='" + authoritiesID.getUsername() + '\'' +
                ", authorities='" + authoritiesID.getRoles() + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Authorities that = (Authorities) o;
        return getUsername().equals(that.getUsername()) &&
                getAuthorities().equals(that.getAuthorities());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUsername(), getAuthorities());
    }
}
