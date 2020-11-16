package net.pladema.establishment.repository;

import net.pladema.establishment.repository.entity.HospitalizationType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HospitalizationTypeRepository extends JpaRepository<HospitalizationType, Short> {
}
