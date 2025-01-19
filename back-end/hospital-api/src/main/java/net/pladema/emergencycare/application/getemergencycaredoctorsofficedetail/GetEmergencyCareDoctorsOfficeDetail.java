package net.pladema.emergencycare.application.getemergencycaredoctorsofficedetail;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import net.pladema.emergencycare.application.exception.EmergencyCareAttentionPlaceException;
import net.pladema.emergencycare.application.exception.EmergencyCareAttentionPlaceExceptionEnum;
import net.pladema.emergencycare.application.getemergencycareepisodeinfoforattentionplacedetail.GetEmergencyCareEpisodeInfoForAttentionPlaceDetail;
import net.pladema.emergencycare.application.port.output.EmergencyCareDoctorsOfficeStorage;
import net.pladema.emergencycare.application.port.output.EmergencyCareEpisodeStorage;

import net.pladema.emergencycare.domain.EmergencyCareDoctorsOfficeDetailBo;
import net.pladema.emergencycare.service.domain.EmergencyCareBo;
import net.pladema.medicalconsultation.doctorsoffice.service.domain.DoctorsOfficeBo;

import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class GetEmergencyCareDoctorsOfficeDetail {

	private final EmergencyCareDoctorsOfficeStorage emergencyCareDoctorsOfficeStorage;
	private final EmergencyCareEpisodeStorage emergencyCareEpisodeStorage;
	private final GetEmergencyCareEpisodeInfoForAttentionPlaceDetail getEmergencyCareEpisodeInfoForAttentionPlaceDetail;

	public EmergencyCareDoctorsOfficeDetailBo run(Integer doctorsOfficeId){
		log.debug("Input GetEmergencyCareDoctorsOfficeDetail parameters -> doctorsOfficeId {}", doctorsOfficeId);
		DoctorsOfficeBo doctorsOffice = getDoctorsOfficeOrFail(doctorsOfficeId);
		EmergencyCareDoctorsOfficeDetailBo result = new EmergencyCareDoctorsOfficeDetailBo(doctorsOffice);
		getEmergencyCareEpisodeOrFail(doctorsOfficeId).ifPresent(ec ->
			getEmergencyCareEpisodeInfoForAttentionPlaceDetail.run(result, ec)
		);
		log.debug("Output -> result {}", result);
		return result;
	}

	private DoctorsOfficeBo getDoctorsOfficeOrFail(Integer doctorsOfficeId){
		return emergencyCareDoctorsOfficeStorage.getById(doctorsOfficeId)
				.orElseThrow(() -> new EmergencyCareAttentionPlaceException(
						EmergencyCareAttentionPlaceExceptionEnum.ATTENTION_PLACE_NOT_FOUND,
						"No se encontró un consultorio con id " + doctorsOfficeId)
				);
	}

	private Optional<EmergencyCareBo> getEmergencyCareEpisodeOrFail(Integer doctorsOfficeId){
		return emergencyCareEpisodeStorage.getByDoctorsOfficeIdInAttention(doctorsOfficeId);
	}
}
