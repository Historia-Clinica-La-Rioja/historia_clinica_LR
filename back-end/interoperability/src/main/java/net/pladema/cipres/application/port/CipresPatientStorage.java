package net.pladema.cipres.application.port;

import net.pladema.cipres.domain.BasicDataPersonBo;

public interface CipresPatientStorage {

	Integer getPatientId(BasicDataPersonBo basicDataPersonBo);

}
