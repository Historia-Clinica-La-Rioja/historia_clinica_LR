package ar.lamansys.odontology.domain.consultation;

import ar.lamansys.odontology.domain.consultation.odontogramDrawings.ToothDrawingsBo;

import java.util.List;

public interface OdontogramDrawingStorage {

    void save(Integer patientId, List<ToothDrawingsBo> teethDrawings);

    List<ToothDrawingsBo> getDrawings(Integer patientId);

}
