package net.pladema.establishment.repository;

import net.pladema.establishment.repository.entity.VClinicalSpecialtySector;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VClinicalSpecialtySectorRepository extends JpaRepository<VClinicalSpecialtySector, Integer> {
}
