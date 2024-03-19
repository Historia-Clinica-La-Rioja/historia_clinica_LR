package ar.lamansys.sgh.clinichistory.application.ports;

import ar.lamansys.sgh.clinichistory.domain.anthropometricgraphic.PercentilesBo;
import ar.lamansys.sgh.clinichistory.domain.anthropometricgraphic.enums.EAnthropometricGraphic;
import ar.lamansys.sgh.clinichistory.domain.ips.enums.EGender;

import java.util.List;

public interface PercentilesStorage {

	List<PercentilesBo> getPercentilesList(EAnthropometricGraphic graphic, EGender gender);

}
