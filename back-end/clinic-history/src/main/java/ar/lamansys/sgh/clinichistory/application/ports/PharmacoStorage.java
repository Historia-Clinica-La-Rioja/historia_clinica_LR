package ar.lamansys.sgh.clinichistory.application.ports;

import ar.lamansys.sgh.clinichistory.domain.ips.PharmacoBo;

public interface PharmacoStorage {

	Integer createPharmaco(PharmacoBo pharmacoBo);

}
