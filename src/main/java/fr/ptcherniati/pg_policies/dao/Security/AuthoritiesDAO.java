package fr.ptcherniati.pg_policies.dao.Security;

import fr.ptcherniati.pg_policies.model.Product;
import fr.ptcherniati.pg_policies.model.security.Authorities;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthoritiesDAO extends JpaRepository<Authorities, String> {
}
