package net.pladema.cipres.application.port;

import net.pladema.cipres.domain.OutpatientConsultationBo;

import java.util.List;

public interface CipresConsultationStorage {

	Integer sendOutpatientConsultations(List<OutpatientConsultationBo> consultations);

	void forwardOutpatientConsultation(OutpatientConsultationBo consultationBo, Integer cipresEncounterId);
}
