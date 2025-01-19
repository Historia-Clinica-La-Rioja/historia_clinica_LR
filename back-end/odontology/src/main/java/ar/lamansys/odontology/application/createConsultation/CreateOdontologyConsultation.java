package ar.lamansys.odontology.application.createConsultation;

import ar.lamansys.odontology.domain.consultation.ConsultationBo;
import ar.lamansys.odontology.domain.consultation.ConsultationResponseBo;
import ar.lamansys.odontology.domain.consultation.CreateOdontologyConsultationServiceRequestBo;

import java.util.List;

public interface CreateOdontologyConsultation {

    ConsultationResponseBo run(ConsultationBo consultationBo, List<CreateOdontologyConsultationServiceRequestBo> consultationServiceRequestsBo);

}
