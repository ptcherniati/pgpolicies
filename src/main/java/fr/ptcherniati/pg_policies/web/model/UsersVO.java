package fr.ptcherniati.pg_policies.web.model;

import fr.ptcherniati.pg_policies.model.security.Authorities;
import fr.ptcherniati.pg_policies.model.security.Users;

import java.net.URI;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public class UsersVO {
    private URI uri;
    private String username;
    private Users users;
    private List<AuthoritiesVO> authorities;

    public UsersVO() {
    }

    public UsersVO(Users users, List<Authorities> authorities) {
        setUsers(users);
        this.authorities = authorities.stream().map(a -> new AuthoritiesVO(a)).collect(Collectors.toList());
    }

    public Users getUsers() {
        return users;
    }

    public void setUsers(Users users) {
        this.username = Optional.ofNullable(users).map(Users::getUsername).orElse(null);
        this.users = users;
    }

    public List<AuthoritiesVO> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(List<AuthoritiesVO> authorities) {
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
        return "UsersVO{" +
                "uri=" + uri +
                ", username='" + username + '\'' +
                ", users=" + users +
                ", authorities=" + authorities +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UsersVO usersVO = (UsersVO) o;
        return getUri().equals(usersVO.getUri()) &&
                getUsername().equals(usersVO.getUsername()) &&
                getUsers().equals(usersVO.getUsers()) &&
                getAuthorities().equals(usersVO.getAuthorities());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUri(), getUsername(), getUsers(), getAuthorities());
    }
}
