package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.entity.CommercialMedicationPresentation;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.entity.CommercialMedicationPresentationPK;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommercialMedicationPresentationRepository extends JpaRepository<CommercialMedicationPresentation, CommercialMedicationPresentationPK> {
}
