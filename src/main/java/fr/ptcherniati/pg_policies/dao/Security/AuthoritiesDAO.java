package fr.ptcherniati.pg_policies.dao.Security;

import fr.ptcherniati.pg_policies.model.security.Authorities;
import fr.ptcherniati.pg_policies.model.security.AuthoritiesID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuthoritiesDAO extends JpaRepository<Authorities, AuthoritiesID> {
    List<Authorities> findByUsername(String ussername);
}
