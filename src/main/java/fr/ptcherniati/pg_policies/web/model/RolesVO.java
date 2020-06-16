package fr.ptcherniati.pg_policies.web.model;

import fr.ptcherniati.pg_policies.model.security.Roles;

import java.net.URI;
import java.util.Objects;

public class RolesVO {
    private URI uri;
    private String nom;

    public RolesVO() {
    }

    public RolesVO(Roles role) {
        this.nom = role.getNom();
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public URI getUri() {
        return uri;
    }

    public void setUri(URI uri) {
        this.uri = uri;
    }

    @Override
    public String toString() {
        return "RolesVO{" +
                "uri=" + uri +
                ", nom='" + nom + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RolesVO rolesVO = (RolesVO) o;
        return getUri().equals(rolesVO.getUri()) &&
                getNom().equals(rolesVO.getNom());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUri(), getNom());
    }
}
