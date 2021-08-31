package ar.lamansys.odontology.application.createConsultation;

import ar.lamansys.odontology.domain.consultation.ConsultationDentalActionBo;
import ar.lamansys.odontology.domain.consultation.odontogramDrawings.ToothDrawingsBo;

import java.util.List;

public interface DrawOdontogramService {

    List<ToothDrawingsBo> run(Integer patientId, List<ConsultationDentalActionBo> actions);

}
