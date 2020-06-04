package fr.ptcherniati.pg_policies.model.aliments;

import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(name = "FK_name_Category", columnNames = {"nom", "category_id"}))
public class Aliment {

    @Id
    @GeneratedValue
    private Long id;

    @Length(min = 3, max = 20, message = "Nom trop long ou trop court. Et oui messages sont plus stylés que ceux de Spring")
    @Column(nullable = false)
    private String nom;

    @ManyToOne
    private Category category;

    //constructeur par défaut
    public Aliment() {
    }

    //constructeur pour nos tests
    public Aliment(String nom, Category category) {
        this.category = category;
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

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category_id) {
        this.category = category_id;
    }

    @Override
    public String toString() {
        return "Aliment{" +
                "nom='" + nom + '\'' +
                ", category_id=" + category +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Aliment aliment = (Aliment) o;
        return getId().equals(aliment.getId()) &&
                getNom().equals(aliment.getNom()) &&
                getCategory().equals(aliment.getCategory());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getNom(), getCategory());
    }
}
