package ar.lamansys.sgh.publicapi.apisumar.application.port.out;

import ar.lamansys.sgh.publicapi.apisumar.domain.ConsultationDetailDataBo;

import java.time.LocalDateTime;
import java.util.List;

public interface ConsultationDetailDataStorage {

	List<ConsultationDetailDataBo> getConsultationsData(String sisaCode, LocalDateTime startDate, LocalDateTime endDate);

}
