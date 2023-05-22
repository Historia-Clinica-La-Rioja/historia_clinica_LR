package net.pladema.cipres.application.port;

import net.pladema.cipres.domain.OutpatientConsultationBo;

import java.util.List;

public interface CipresConsultationStorage {

	void createOutpatientConsultations(List<OutpatientConsultationBo> consultations);

}
