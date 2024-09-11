package net.pladema.emergencycare.application.getemergencycareepisodeinfoforattentionplacedetail;

import lombok.RequiredArgsConstructor;

import net.pladema.emergencycare.application.port.output.EmergencyCareEpisodeStorage;
import net.pladema.emergencycare.application.port.output.EmergencyCarePatientStorage;
import net.pladema.emergencycare.application.port.output.EmergencyCareTriageCategoryStorage;
import net.pladema.emergencycare.domain.EmergencyCareBedDetailBo;

import net.pladema.emergencycare.domain.EmergencyCarePatientBo;
import net.pladema.emergencycare.service.domain.EmergencyCareBo;

import net.pladema.emergencycare.triage.application.fetchlasttriagebyemergencycareepisodeid.FetchLastTriageByEmergencyCareEpisodeId;
import net.pladema.emergencycare.triage.domain.TriageBo;

import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class GetEmergencyCareEpisodeInfoForAttentionPlaceDetail {

	private final EmergencyCarePatientStorage emergencyCarePatientStorage;
	private final EmergencyCareEpisodeStorage emergencyCareEpisodeStorage;
	private final FetchLastTriageByEmergencyCareEpisodeId fetchLastTriageByEmergencyCareEpisodeId;
	private final EmergencyCareTriageCategoryStorage emergencyCareTriageCategoryStorage;

	public void run(EmergencyCareBedDetailBo ecbd, EmergencyCareBo ec){
		if(ec.getPatient() != null) {
			EmergencyCarePatientBo ecp = emergencyCarePatientStorage.getById(ec.getPatient().getId());
			ecp.setPatientDescription(ec.getPatient().getPatientDescription());
			ecbd.setPatient(ecp);
		}
		ecbd.setReason(ec.getReason());
		ecbd.setEmergencyCareTypeId(ec.getEmergencyCareTypeId());
		ecbd.setEmergencyCareStateId(ec.getEmergencyCareStateId());
		ecbd.setProfessional(emergencyCareEpisodeStorage.getProfessionalByEpisodeId(ec.getId()));
		TriageBo lastTriage = fetchLastTriageByEmergencyCareEpisodeId.run(ec.getId());
		if (lastTriage != null){
			ecbd.setLastTriage(lastTriage);
			ecbd.setTriageCategoryInfo(emergencyCareTriageCategoryStorage.getById(lastTriage.getCategoryId()));
		}
	}
}
