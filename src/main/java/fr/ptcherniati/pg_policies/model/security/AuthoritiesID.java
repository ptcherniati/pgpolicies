package fr.ptcherniati.pg_policies.model.security;

import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class AuthoritiesID implements Serializable {

    protected String username;
    @ManyToOne
    protected Roles roles;

    public AuthoritiesID() {
    }

    public AuthoritiesID(Authorities authorities) {
        this.username = authorities.getUsername();
        this.roles = authorities.getRoles();
    }

    public AuthoritiesID(String username, Roles role) {
        this.username = username;
        this.roles = role;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Roles getRoles() {
        return roles;
    }

    public void setRoles(Roles roles) {
        this.roles = roles;
    }

    @Override
    public String toString() {
        return "AuthoritiesID{" +
                "username='" + username + '\'' +
                ", roles=" + roles +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AuthoritiesID that = (AuthoritiesID) o;
        return getUsername().equals(that.getUsername()) &&
                getRoles().equals(that.getRoles());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUsername(), getRoles());
    }
}
