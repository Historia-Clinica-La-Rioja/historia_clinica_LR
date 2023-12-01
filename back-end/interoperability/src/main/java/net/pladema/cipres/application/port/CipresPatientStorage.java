package net.pladema.cipres.application.port;

import net.pladema.cipres.domain.BasicDataPatientBo;
import net.pladema.cipres.domain.PersonDataBo;

import java.util.Optional;

public interface CipresPatientStorage {

	Optional<Long> getPatientId(BasicDataPatientBo basicDataPatientBo, String establishmentId);

	Optional<Long> createPatient(PersonDataBo person, String establishmentId);

}
