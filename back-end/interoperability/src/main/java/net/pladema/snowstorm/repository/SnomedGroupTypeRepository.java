package net.pladema.snowstorm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import net.pladema.snowstorm.repository.entity.SnomedGroupType;

@Repository
public interface SnomedGroupTypeRepository extends JpaRepository<SnomedGroupType, Short> {

}
