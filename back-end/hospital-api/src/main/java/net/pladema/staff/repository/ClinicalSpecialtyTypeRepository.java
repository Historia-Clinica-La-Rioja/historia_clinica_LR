package net.pladema.staff.repository;

import net.pladema.staff.repository.entity.ClinicalSpecialtyType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClinicalSpecialtyTypeRepository extends JpaRepository<ClinicalSpecialtyType, Short> {
}
