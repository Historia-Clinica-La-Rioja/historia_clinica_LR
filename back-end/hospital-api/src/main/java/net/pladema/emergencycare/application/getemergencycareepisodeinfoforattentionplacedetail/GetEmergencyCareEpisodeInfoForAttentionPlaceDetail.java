package net.pladema.emergencycare.application.getemergencycareepisodeinfoforattentionplacedetail;

import lombok.RequiredArgsConstructor;

import net.pladema.emergencycare.application.port.output.EmergencyCareEpisodeStorage;
import net.pladema.emergencycare.application.port.output.EmergencyCarePatientStorage;
import net.pladema.emergencycare.application.port.output.EmergencyCareTriageCategoryStorage;
import net.pladema.emergencycare.application.port.output.HistoricEmergencyEpisodeStorage;
import net.pladema.emergencycare.domain.EmergencyCareAttentionPlaceDetailBo;

import net.pladema.emergencycare.domain.EmergencyCarePatientBo;
import net.pladema.emergencycare.service.domain.EmergencyCareBo;

import net.pladema.emergencycare.service.domain.HistoricEmergencyEpisodeBo;
import net.pladema.emergencycare.triage.application.fetchlasttriagebyemergencycareepisodeid.FetchLastTriageByEmergencyCareEpisodeId;
import net.pladema.emergencycare.triage.domain.EmergencyCareTriageBo;
import net.pladema.emergencycare.triage.domain.TriageBo;

import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class GetEmergencyCareEpisodeInfoForAttentionPlaceDetail {

	private final EmergencyCarePatientStorage emergencyCarePatientStorage;
	private final EmergencyCareEpisodeStorage emergencyCareEpisodeStorage;
	private final FetchLastTriageByEmergencyCareEpisodeId fetchLastTriageByEmergencyCareEpisodeId;
	private final EmergencyCareTriageCategoryStorage emergencyCareTriageCategoryStorage;
	private final HistoricEmergencyEpisodeStorage historicEmergencyEpisodeStorage;

	public void run(EmergencyCareAttentionPlaceDetailBo ecap, EmergencyCareBo ec){
		if(ec.getPatient() != null) {
			EmergencyCarePatientBo ecp = emergencyCarePatientStorage.getById(ec.getPatient().getId());
			ecp.setPatientDescription(ec.getPatient().getPatientDescription());
			ecap.setPatient(ecp);
		}
		Optional<HistoricEmergencyEpisodeBo> heeOpt = historicEmergencyEpisodeStorage.getLatestByEpisodeId(ec.getId());
		ecap.setUpdatedOn(heeOpt.isPresent() ? heeOpt.get().getChangeStateDate() : ec.getCreatedOn());
		ecap.setReason(ec.getReason());
		ecap.setEmergencyCareTypeId(ec.getEmergencyCareTypeId());
		ecap.setEmergencyCareStateId(ec.getEmergencyCareStateId());
		ecap.setProfessional(emergencyCareEpisodeStorage.getProfessionalByEpisodeId(ec.getId()));
		ecap.setEpisodeId(ec.getId());
		TriageBo lastTriage = fetchLastTriageByEmergencyCareEpisodeId.run(ec.getId());
		if (lastTriage != null){
			ecap.setLastTriage(new EmergencyCareTriageBo(
					lastTriage,
					emergencyCareTriageCategoryStorage.getById(lastTriage.getCategoryId())
			));
		}
	}
}
