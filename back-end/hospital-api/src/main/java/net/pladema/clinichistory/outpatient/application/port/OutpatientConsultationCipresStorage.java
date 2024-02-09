package net.pladema.clinichistory.outpatient.application.port;

import net.pladema.clinichistory.outpatient.createoutpatient.service.domain.OutpatientBasicDataBo;

import java.util.List;

public interface OutpatientConsultationCipresStorage {

	List<OutpatientBasicDataBo> getOutpatientConsultationsToCipres();

}
