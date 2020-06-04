package fr.ptcherniati.pg_policies.web.model;

import fr.ptcherniati.pg_policies.model.aliments.Aliment;

import java.net.URI;
import java.util.Objects;

public class AlimentVO {
    private URI uri;
    private long id;
    private String nom;
    private CategoryVO categoryVO;

    public AlimentVO() {
    }

    public AlimentVO(Aliment aliment) {
        this.id = aliment.getId();
        this.nom = aliment.getNom();
        this.categoryVO = new CategoryVO(aliment.getCategory());
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

    public CategoryVO getCategoryVO() {
        return categoryVO;
    }

    public void setCategoryVO(CategoryVO categoryVO) {
        this.categoryVO = categoryVO;
    }

    @Override
    public String toString() {
        return "AlimentVO{" +
                "uri=" + uri +
                ", id=" + id +
                ", nom='" + nom + '\'' +
                ", categoryVO=" + categoryVO +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AlimentVO alimentVO = (AlimentVO) o;
        return getId() == alimentVO.getId() &&
                getUri().equals(alimentVO.getUri()) &&
                getNom().equals(alimentVO.getNom()) &&
                getCategoryVO().equals(alimentVO.getCategoryVO());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUri(), getId(), getNom(), getCategoryVO());
    }
}
