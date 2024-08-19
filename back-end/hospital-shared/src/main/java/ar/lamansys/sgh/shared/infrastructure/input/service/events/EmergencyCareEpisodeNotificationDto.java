package ar.lamansys.sgh.shared.infrastructure.input.service.events;

import ar.lamansys.sgh.shared.domain.EmergencyCareEpisodeNotificationBo;
import ar.lamansys.sgh.shared.infrastructure.input.service.SharedTriageCategoryDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class EmergencyCareEpisodeNotificationDto {

	private Integer episodeId;

	private String patientName;

	private String doctorName;

	private String placeName;

	private String topic;

	private SharedTriageCategoryDto triageCategory;

	public EmergencyCareEpisodeNotificationDto(EmergencyCareEpisodeNotificationBo notificationData) {
		this.episodeId = notificationData.getEpisodeId();
		this.patientName = notificationData.getPatientName();
		this.doctorName = notificationData.getDoctorName();
		this.placeName = notificationData.getPlaceName();
		this.topic = notificationData.getTopic();
		this.triageCategory = new SharedTriageCategoryDto(notificationData.getTriageCategory());
	}

}
