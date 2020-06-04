package fr.ptcherniati.pg_policies.model.aliments;

import org.hibernate.validator.constraints.Length;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Objects;

@Entity
public class Category {

    @Id
    @GeneratedValue
    private Long id;

    @Length(min = 3, max = 20, message = "Nom trop long ou trop court. Et oui messages sont plus stylés que ceux de Spring")
    private String nom;

    //constructeur par défaut
    public Category() {
    }

    //constructeur pour nos tests
    public Category(String nom) {
        this.nom = nom;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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
        return "Category{" +
                "id=" + id + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Category category = (Category) o;
        return getId().equals(category.getId()) &&
                getNom().equals(category.getNom());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getNom());
    }
}
