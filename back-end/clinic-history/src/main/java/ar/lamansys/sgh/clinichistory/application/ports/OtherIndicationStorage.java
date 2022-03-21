package ar.lamansys.sgh.clinichistory.application.ports;

import java.util.List;

import ar.lamansys.sgh.clinichistory.domain.ips.OtherIndicationBo;

public interface OtherIndicationStorage {

	Integer createOtherIndication(OtherIndicationBo otherIndicationBo);
}
