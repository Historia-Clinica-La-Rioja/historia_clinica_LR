package ar.lamansys.sgh.clinichistory.application.ports;

import ar.lamansys.sgh.clinichistory.domain.anthropometricgraphic.ZScoreBo;
import ar.lamansys.sgh.clinichistory.domain.anthropometricgraphic.enums.EAnthropometricGraphic;
import ar.lamansys.sgh.clinichistory.domain.ips.enums.EGender;

import java.util.List;

public interface ZScoreStorage {

	List<ZScoreBo> getZScoreList(EAnthropometricGraphic graphic, EGender gender);

}
