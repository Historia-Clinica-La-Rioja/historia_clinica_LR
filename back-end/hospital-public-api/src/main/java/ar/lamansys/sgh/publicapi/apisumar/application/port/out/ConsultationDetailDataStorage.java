package ar.lamansys.sgh.publicapi.apisumar.application.port.out;

import ar.lamansys.sgh.publicapi.apisumar.domain.ConsultationDetailDataBo;

import java.util.List;

public interface ConsultationDetailDataStorage {

	List<ConsultationDetailDataBo> getConsultationsData(String sisaCode);

}
