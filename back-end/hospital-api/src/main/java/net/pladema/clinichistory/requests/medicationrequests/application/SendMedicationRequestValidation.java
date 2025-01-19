package net.pladema.clinichistory.requests.medicationrequests.application;

import ar.lamansys.sgh.clinichistory.domain.document.impl.MedicationRequestBo;

import java.util.List;

public interface SendMedicationRequestValidation {

	List<String> run(MedicationRequestBo medicationRequest);

}
