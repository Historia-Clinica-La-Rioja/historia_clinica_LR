package net.pladema.clinichistory.outpatient.application.port;

import net.pladema.clinichistory.outpatient.createoutpatient.service.domain.OutpatientBasicDataBo;

import java.util.List;
import java.util.Map;

public interface OutpatientConsultationStorage {

	List<OutpatientBasicDataBo> getOutpatientConsultationsToCipres();

}
