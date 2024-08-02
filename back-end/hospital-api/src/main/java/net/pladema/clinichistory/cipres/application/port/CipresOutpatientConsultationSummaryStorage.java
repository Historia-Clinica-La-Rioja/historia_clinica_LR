package net.pladema.clinichistory.cipres.application.port;

import net.pladema.clinichistory.cipres.domain.CipresOutpatientBasicDataBo;

import java.time.LocalDateTime;
import java.util.List;

public interface CipresOutpatientConsultationSummaryStorage {

	List<CipresOutpatientBasicDataBo> getOutpatientConsultationsForSend(Integer limit, LocalDateTime start, LocalDateTime end);

	List<CipresOutpatientBasicDataBo> getOutpatientConsultationsForSendOrResend(Integer limit, LocalDateTime start, LocalDateTime end);

	CipresOutpatientBasicDataBo getOutpatientConsultationByCipresEncounterId(Integer cipresEncounterId);

}
