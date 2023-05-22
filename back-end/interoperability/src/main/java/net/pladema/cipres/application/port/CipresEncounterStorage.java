package net.pladema.cipres.application.port;

import net.pladema.cipres.domain.CipresEncounterBo;
import net.pladema.cipres.domain.OutpatientConsultationBo;

public interface CipresEncounterStorage {

	String getClinicalSpecialtiyBySnomedCode(String snomedCode);
	
	String getEstablishmentByRefesCode(String refesCode);

	CipresEncounterBo createOutpatientConsultation(OutpatientConsultationBo consultation,
													 String clinicalSpecialtyIRI,
													 String establishmentIRI);

}
