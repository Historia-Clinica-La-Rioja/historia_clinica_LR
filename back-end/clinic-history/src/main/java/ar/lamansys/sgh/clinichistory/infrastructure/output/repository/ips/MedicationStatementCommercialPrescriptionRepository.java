package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.entity.MedicationStatementCommercialPrescription;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MedicationStatementCommercialPrescriptionRepository extends JpaRepository<MedicationStatementCommercialPrescription, Integer> {
}
