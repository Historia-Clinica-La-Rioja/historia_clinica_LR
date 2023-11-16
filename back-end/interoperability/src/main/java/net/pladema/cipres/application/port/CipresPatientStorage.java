package net.pladema.cipres.application.port;

import net.pladema.cipres.domain.BasicDataPersonBo;
import net.pladema.cipres.domain.PersonDataBo;

import java.util.Optional;

public interface CipresPatientStorage {

	Optional<Integer> getPatientId(BasicDataPersonBo basicDataPersonBo, String establishmentId);

	Optional<Integer> createPatient(PersonDataBo person, String establishmentId);

}
