package ar.lamansys.sgh.clinichistory.application.ports;

import ar.lamansys.sgh.clinichistory.domain.ips.DietBo;

import java.util.List;
import java.util.Optional;

public interface DietStorage {

	List<DietBo> getInternmentEpisodeDiets(Integer internmentEpisodeId);

	Integer createDiet(DietBo dietBo);

	Optional<DietBo> findById(Integer id);

}
