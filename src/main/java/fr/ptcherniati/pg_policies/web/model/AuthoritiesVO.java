package fr.ptcherniati.pg_policies.web.model;

import fr.ptcherniati.pg_policies.model.security.Authorities;

import java.net.URI;
import java.util.Objects;

public class AuthoritiesVO {
    private URI uri;
    private String username;
    private String authorities;

    public AuthoritiesVO() {
    }

    public AuthoritiesVO(Authorities authorities) {
        this.authorities = authorities.getAuthorities();
        this.username = authorities.getUsername();
    }

    public String getAuthorities() {
        return authorities;
    }

    public void setAuthorities(String authorities) {
        this.authorities = authorities;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public URI getUri() {
        return uri;
    }

    public void setUri(URI uri) {
        this.uri = uri;
    }

    @Override
    public String toString() {
        return "AuthoritiesVO{" +
                "uri=" + uri +
                ", username='" + username + '\'' +
                ", authorities='" + authorities + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AuthoritiesVO that = (AuthoritiesVO) o;
        return getUri().equals(that.getUri()) &&
                getUsername().equals(that.getUsername()) &&
                getAuthorities().equals(that.getAuthorities());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUri(), getUsername(), getAuthorities());
    }
}
