package ar.lamansys.odontology.application.createConsultation;

import ar.lamansys.odontology.domain.consultation.ConsultationBo;
import ar.lamansys.odontology.domain.consultation.ConsultationResponseBo;

public interface CreateOdontologyConsultation {

    ConsultationResponseBo run(ConsultationBo consultationBo);

}
