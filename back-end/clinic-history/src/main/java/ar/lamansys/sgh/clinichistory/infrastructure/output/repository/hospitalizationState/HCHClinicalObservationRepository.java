package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.hospitalizationState;


import ar.lamansys.sgh.clinichistory.domain.ips.MapClinicalObservationVo;
import java.util.List;

public interface HCHClinicalObservationRepository {

    MapClinicalObservationVo getGeneralState(Integer internmentEpisodeId, List<Short> invalidDocumentTypes);
}
