package fr.ptcherniati.pg_policies.model.security;

import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class AuthoritiesID implements Serializable {

    protected String username;
    protected String authority;

    public AuthoritiesID() {
    }

    public AuthoritiesID(Authorities authorities) {
        this.username = authorities.getUsername();
        this.authority = authorities.getAuthorities();
    }

    public AuthoritiesID(String username, String authority) {
        this.username = username;
        this.authority = authority;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

    @Override
    public String toString() {
        return "Authorities{" +
                "username='" + username + '\'' +
                ", authority='" + authority + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AuthoritiesID that = (AuthoritiesID) o;
        return getUsername().equals(that.getUsername()) &&
                getAuthority().equals(that.getAuthority());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUsername(), getAuthority());
    }
}
