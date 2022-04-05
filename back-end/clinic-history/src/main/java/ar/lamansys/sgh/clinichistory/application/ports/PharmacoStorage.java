package ar.lamansys.sgh.clinichistory.application.ports;

import ar.lamansys.sgh.clinichistory.domain.ips.PharmacoBo;
import ar.lamansys.sgh.clinichistory.domain.ips.PharmacoSummaryBo;

import java.util.List;

public interface PharmacoStorage {

	Integer createPharmaco(PharmacoBo pharmacoBo);

	List<PharmacoSummaryBo> getInternmentEpisodePharmacos(Integer internmentEpisodeId);

}
