package fr.ptcherniati.pg_policies.web.model;

import fr.ptcherniati.pg_policies.model.security.Authorities;
import fr.ptcherniati.pg_policies.model.security.Users;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class UsersVO {
    private String username;
    private Users users;
    private List<Authorities> authorities;

    public UsersVO() {
    }

    public UsersVO(Users users, List<Authorities> authorities) {
        setUsers(users);
        this.authorities = authorities;
    }

    public Users getUsers() {
        return users;
    }

    public void setUsers(Users users) {
        this.username = Optional.ofNullable(users).map(Users::getUsername).orElse(null);
        this.users = users;
    }

    public List<Authorities> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(List<Authorities> authorities) {
        this.authorities = authorities;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return "UsersVO{" +
                "username=" + username +
                "users=" + users +
                ", authorities=" + authorities +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UsersVO usersVO = (UsersVO) o;
        return getUsers().equals(usersVO.getUsers()) &&
                getAuthorities().equals(usersVO.getAuthorities());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUsers(), getAuthorities());
    }
}
