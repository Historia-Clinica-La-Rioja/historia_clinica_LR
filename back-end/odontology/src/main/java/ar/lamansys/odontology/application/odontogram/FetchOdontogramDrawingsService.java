package ar.lamansys.odontology.application.odontogram;

import ar.lamansys.odontology.domain.consultation.odontogramDrawings.ToothDrawingsBo;

import java.util.List;

public interface FetchOdontogramDrawingsService {

    List<ToothDrawingsBo> run(Integer patientId);

}
