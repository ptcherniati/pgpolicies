package fr.ptcherniati.pg_policies.model.security;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@Entity
//@JsonFilter("nameFilter")
public class Policies {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id", unique = true, nullable = false)
    Long id;

    @ManyToOne
    private Roles nom;
    private String description;
    @Column(name = "using_field")
    private String using;
    @Column(name = "check_field")
    private String check;
    @Column(name = "for_field")
    private String _for;

    public Policies() {
    }

    public Policies(Long id, @NotNull Roles nom, String description, String using, String check, @NotNull String _for) {
        this.id = id;
        this.nom = nom;
        this.description = description;
        this.using = using;
        this.check = check;
        this._for = _for;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Roles getNom() {
        return nom;
    }

    public void setNom(Roles nom) {
        this.nom = nom;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUsing() {
        return using;
    }

    public void setUsing(String using) {
        this.using = using;
    }

    public String getCheck() {
        return check;
    }

    public void setCheck(String check) {
        this.check = check;
    }

    public String getFor() {
        return _for;
    }

    public void setFor(String _for) {
        this._for = _for;
    }

    @Override
    public String toString() {
        return "Poilicies{" +
                "id=" + id +
                ", nom=" + nom +
                ", description='" + description + '\'' +
                ", using='" + using + '\'' +
                ", check='" + check + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Policies policies = (Policies) o;
        return getId().equals(policies.getId()) &&
                getNom().equals(policies.getNom()) &&
                Objects.equals(getDescription(), policies.getDescription()) &&
                Objects.equals(getUsing(), policies.getUsing()) &&
                Objects.equals(getCheck(), policies.getCheck());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getNom(), getDescription(), getUsing(), getCheck());
    }
}
