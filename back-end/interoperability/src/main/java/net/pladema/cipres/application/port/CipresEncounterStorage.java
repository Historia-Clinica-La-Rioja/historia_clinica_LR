package net.pladema.cipres.application.port;

import net.pladema.cipres.domain.CipresEncounterBo;
import net.pladema.cipres.domain.OutpatientConsultationBo;
import net.pladema.cipres.infrastructure.output.rest.domain.consultation.CipresEstablishmentResponse;

import java.util.Optional;

public interface CipresEncounterStorage {

	Optional<String> getClinicalSpecialtiyBySnomedCode(String snomedCode);

	Optional<CipresEstablishmentResponse> getEstablishmentBySisaCode(String sisaCode);

	CipresEncounterBo createOutpatientConsultation(OutpatientConsultationBo consultation, String clinicalSpecialtyIRI, String establishmentIRI);

}
