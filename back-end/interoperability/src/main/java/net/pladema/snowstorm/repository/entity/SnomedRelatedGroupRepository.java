package net.pladema.snowstorm.repository.entity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SnomedRelatedGroupRepository extends JpaRepository<SnomedRelatedGroup, Integer> {
}
