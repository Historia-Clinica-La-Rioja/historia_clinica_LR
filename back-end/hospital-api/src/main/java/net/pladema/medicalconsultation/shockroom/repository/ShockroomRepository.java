package net.pladema.medicalconsultation.shockroom.repository;

import ar.lamansys.sgx.shared.auditable.repository.SGXAuditableEntityJPARepository;
import net.pladema.medicalconsultation.shockroom.repository.entity.Shockroom;

public interface ShockroomRepository extends SGXAuditableEntityJPARepository<Shockroom, Integer> {
}
