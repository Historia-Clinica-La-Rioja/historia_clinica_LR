package ar.lamansys.sgh.clinichistory.application.ports;

import java.util.List;
import java.util.Optional;

import ar.lamansys.sgh.clinichistory.domain.ips.OtherIndicationBo;

public interface OtherIndicationStorage {

	List<OtherIndicationBo> getInternmentEpisodeOtherIndications(Integer internmentEpisodeId);

	Integer createOtherIndication(OtherIndicationBo otherIndicationBo);

	Optional<OtherIndicationBo> findById(Integer id);

}
