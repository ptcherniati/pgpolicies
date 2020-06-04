package fr.ptcherniati.pg_policies.web.model;

import fr.ptcherniati.pg_policies.model.security.Policies;
import fr.ptcherniati.pg_policies.model.security.Roles;

import java.net.URI;
import java.util.Objects;

public class PoliciesVO {
    private URI uri;
    private Long id;
    private Roles nom;
    private String description;
    private String using;
    private String check;
    private String _for;

    public PoliciesVO() {
    }

    public PoliciesVO(Policies policies) {
        this.id = policies.getId();
        this.nom = policies.getNom();
        this.description = policies.getDescription();
        this.using = policies.getUsing();
        this.check = policies.getCheck();
        this._for = policies.getFor();
    }

    public URI getUri() {
        return uri;
    }

    public void setUri(URI uri) {
        this.uri = uri;
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
        return "PoliciesVO{" +
                "uri=" + uri +
                ", id=" + id +
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
        PoliciesVO that = (PoliciesVO) o;
        return getUri().equals(that.getUri()) &&
                getId().equals(that.getId()) &&
                getNom().equals(that.getNom()) &&
                Objects.equals(getDescription(), that.getDescription()) &&
                Objects.equals(getUsing(), that.getUsing()) &&
                Objects.equals(getCheck(), that.getCheck());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUri(), getId(), getNom(), getDescription(), getUsing(), getCheck());
    }
}
