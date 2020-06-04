package fr.ptcherniati.pg_policies.dao.Security;

import fr.ptcherniati.pg_policies.model.security.Policies;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PoliciesDAO extends JpaRepository<Policies, Long> {
}
