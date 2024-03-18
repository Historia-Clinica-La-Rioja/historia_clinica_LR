package net.pladema.cipres.application.port;

import net.pladema.cipres.domain.BasicDataPatientBo;
import net.pladema.cipres.domain.PersonDataBo;
import net.pladema.cipres.infrastructure.output.rest.domain.patient.CipresPatientAddressPayload;

import java.util.Optional;

public interface CipresPatientStorage {

	Optional<Long> getPatientId(BasicDataPatientBo basicDataPatientBo, String establishmentId,
								Integer encounterId, Integer cipresEncounterId,
								Integer institutionId);

	Optional<Long> createPatient(PersonDataBo person, String establishmentId, CipresPatientAddressPayload address);

}
