package net.pladema.clinichistory.cipres.application.port;

import net.pladema.clinichistory.cipres.domain.CipresOutpatientBasicDataBo;

import java.util.List;

public interface CipresOutpatientConsultationStorage {

	List<CipresOutpatientBasicDataBo> getOutpatientConsultations();

	CipresOutpatientBasicDataBo getOutpatientConsultationData(Integer cipresEncounterId);

}
