package net.pladema.medicalconsultation.unsitisfiedDemand.infrastructure.output.repository;

import ar.lamansys.sgx.shared.auditable.repository.SGXAuditableEntityJPARepository;
import net.pladema.medicalconsultation.unsitisfiedDemand.infrastructure.output.entity.UnsatisfiedAppointmentDemand;

import org.springframework.stereotype.Repository;

@Repository
public interface UnsatisfiedAppointmentDemandRepository extends SGXAuditableEntityJPARepository<UnsatisfiedAppointmentDemand, Integer> {
}
