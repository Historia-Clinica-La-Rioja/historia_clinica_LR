package net.pladema.establishment.repository;

import net.pladema.establishment.repository.entity.VClinicalServiceSector;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VClinicalServiceSectorRepository extends JpaRepository<VClinicalServiceSector, Integer> {
}
