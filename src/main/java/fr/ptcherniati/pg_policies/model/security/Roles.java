package fr.ptcherniati.pg_policies.model.security;

import org.hibernate.validator.constraints.Length;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.Objects;

@Entity
//@JsonFilter("nameFilter")
public class Roles {
    @Id
    @NotNull
    @Length(min = 6, max = 20, message = "Le role doit être entre 4 et 20 caractères.")
    @Pattern(regexp = "(ROLE|policy)_.*", message = "Le role doit commencer par ROLE_ ou policy_ (exemple ROLE_DELETE).")
    private String nom;

    public Roles() {
    }

    public Roles(String nom) {
        this.nom = nom;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    @Override
    public String toString() {
        return "Roles{" +
                "nom='" + nom + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Roles roles = (Roles) o;
        return Objects.equals(getNom(), roles.getNom());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getNom());
    }
}
