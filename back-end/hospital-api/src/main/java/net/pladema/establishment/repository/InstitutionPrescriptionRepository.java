package net.pladema.establishment.repository;

import net.pladema.establishment.repository.entity.InstitutionPrescription;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InstitutionPrescriptionRepository extends JpaRepository<InstitutionPrescription, Integer> {
}
