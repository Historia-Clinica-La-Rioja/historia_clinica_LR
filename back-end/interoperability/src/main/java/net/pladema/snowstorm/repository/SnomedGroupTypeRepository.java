package net.pladema.snowstorm.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import net.pladema.snowstorm.repository.entity.SnomedGroupType;

public interface SnomedGroupTypeRepository extends JpaRepository<SnomedGroupType, Short> {

}
