package fr.ptcherniati.pg_policies.dao.aliment;

import fr.ptcherniati.pg_policies.model.aliments.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategorieDAO extends JpaRepository<Category, Long> {
}
