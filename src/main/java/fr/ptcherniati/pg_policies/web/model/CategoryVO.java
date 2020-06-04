package fr.ptcherniati.pg_policies.web.model;

import fr.ptcherniati.pg_policies.model.aliments.Category;

import java.net.URI;
import java.util.Objects;

public class CategoryVO {
    private URI uri;
    private long id;
    private String nom;

    public CategoryVO() {
    }

    public CategoryVO(Category category) {
        this.id = category.getId();
        this.nom = category.getNom();
    }

    public URI getUri() {
        return uri;
    }

    public void setUri(URI uri) {
        this.uri = uri;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    @Override
    public String toString() {
        return "CategoryVO{" +
                "uri=" + uri +
                ", id=" + id +
                ", nom='" + nom + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CategoryVO that = (CategoryVO) o;
        return getId() == that.getId() &&
                getUri().equals(that.getUri()) &&
                getNom().equals(that.getNom());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUri(), getId(), getNom());
    }
}
