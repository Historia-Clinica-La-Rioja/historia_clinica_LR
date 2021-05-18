package net.pladema.establishment.repository;

import net.pladema.establishment.repository.entity.Dependency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DependencyRepository extends JpaRepository<Dependency, Integer> {
}
