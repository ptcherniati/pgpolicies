package fr.ptcherniati.pg_policies.dao;

import com.google.common.base.Strings;
import fr.ptcherniati.pg_policies.model.security.Authorities;
import fr.ptcherniati.pg_policies.model.security.AuthoritiesID;
import fr.ptcherniati.pg_policies.model.security.Policies;
import fr.ptcherniati.pg_policies.model.security.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.sql.DataSource;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
public class PgPoliciesDAO{
    @PersistenceContext
    private EntityManager em;

    @Transactional
    public String setUser(String role){
        return  (String) em.createNativeQuery("select username from users")
                .setMaxResults(1)
                .getSingleResult();
    }
    @Transactional
    public Users initUser(Users users){
            String sql  = String.format("create role %s;grant role_user to %1$s; ", users.getUsername());
            int update = em.createNativeQuery(sql).executeUpdate();
            return users;
        }

    @Transactional
    public void use(String username) {
            String sql  = String.format("set role \"%s\"; ", username);
            int update = em.createNativeQuery(sql).executeUpdate();
    }

    @Transactional
    public Optional<String> getRole(String role) {
            String sql  = String.format("select rolname from pg_roles\n" +
                    "where rolname =  '%s'; ", role);
            return em.createNativeQuery(sql).getResultList().stream().findFirst();
    }

    @Transactional
    public void createRole(String role) {
            String sql  = String.format("create role \"%1$s\"", role);
            int update = em.createNativeQuery(sql).executeUpdate();
    }

    @Transactional
    public void dropRole(String role) {
            String sql  = String.format("drop role \"%1$s\"", role);
            int update = em.createNativeQuery(sql).executeUpdate();
    }

    @Transactional
    public void createPolicy(Policies policy) {
            String sql  = String.format(
                    "create policy pa_%1$s\n" +
                    " on aliment  as PERMISSIVE \n" +
                    " for %2$s \n" +
                    " to %1$s \n" +
                    Optional.ofNullable(policy).map(p->p.getUsing()).map(u->" using ( nom ~* '%3$s') \n").orElse("")+
                    Optional.ofNullable(policy)
                            .filter(p->!"SELECT".equals(p.getFor()) && "DELETE".equals(p.getFor())  )
                            .map(p->p.getCheck())
                            .map(u->" with check ( nom ~* '%4$s') \n").orElse(""),
                    policy.getNom().getNom(),
                    policy.getFor(),
                    policy.getUsing(),
                    policy.getCheck());
            int update = em.createNativeQuery(sql).executeUpdate();
    }

    @Transactional
    public void dropPolicy(Policies policy) {
            String sql  = String.format(
                    "drop policy pa_%1$s\n" +
                    " on aliment" ,
                    policy.getNom().getNom()
            );
            int update = em.createNativeQuery(sql).executeUpdate();
    }

    @Transactional
    public void addToGroup(Authorities authorities) {
            String sql  = String.format("grant \"%1$s\" to \"%2$s\" ", authorities.getRoles().getNom(), authorities.getUsername());
            int update = em.createNativeQuery(sql).executeUpdate();
    }

    @Transactional
    public void removeToGroup(Authorities authorities) {
            String sql  = String.format("revoke \"%1$s\" from \"%2$s\" ", authorities.getRoles().getNom(), authorities.getUsername());
            int update = em.createNativeQuery(sql).executeUpdate();
    }
}
