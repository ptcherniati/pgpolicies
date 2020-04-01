package fr.ptcherniati.pg_policies.model.security;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import java.util.Objects;
import java.util.Optional;

@Entity
@NamedQuery(name = "Authorities.findByUsername", query = "select a from Authorities a where a.authoritiesID.username = ?1")
public class Authorities {
    @Embedded
    @Id
    AuthoritiesID authoritiesID = new AuthoritiesID();

    public Authorities() {
    }

    public Authorities(String username, String authorities) {
        this.authoritiesID.setUsername(username);
        this.authoritiesID.setAuthority(authorities);
    }

    public String getUsername() {
        return Optional.ofNullable(authoritiesID).map(AuthoritiesID::getUsername).orElse(null);
    }

    public void setUsername(String username) {
        Optional.ofNullable(authoritiesID).orElseGet(AuthoritiesID::new);
    }

    public String getAuthorities() {
        return Optional.ofNullable(authoritiesID).map(authoritiesID1 -> authoritiesID1.getAuthority()).orElse(null);
    }

    public void setAuthorities(String authorities) {
        this.authoritiesID.setAuthority(authorities);
    }

    @Override
    public String toString() {
        return "Authorities{" +
                "username='" + authoritiesID.getUsername() + '\'' +
                ", authorities='" + authoritiesID.getAuthority() + '\'' +
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
