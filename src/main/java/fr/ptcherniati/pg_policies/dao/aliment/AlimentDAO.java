package fr.ptcherniati.pg_policies.dao.aliment;

import fr.ptcherniati.pg_policies.model.aliments.Aliment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AlimentDAO extends JpaRepository<Aliment, Long> {
}
