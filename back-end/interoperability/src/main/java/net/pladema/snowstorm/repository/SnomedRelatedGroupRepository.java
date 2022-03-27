package net.pladema.snowstorm.repository;

import net.pladema.snowstorm.repository.entity.SnomedRelatedGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SnomedRelatedGroupRepository extends JpaRepository<SnomedRelatedGroup, Integer> {
}
