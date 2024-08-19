package ar.lamansys.sgh.shared.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmergencyCareEpisodeNotificationBo {

	private Integer episodeId;

	private String patientName;

	private String doctorName;

	private String placeName;

	private TriageCategoryBo triageCategory;

	private String topic;

	public EmergencyCareEpisodeNotificationBo(Integer episodeId, String patientFirstName, String patientLastName, String placeName, String triageCategoryName,
											  String colorCode, String topic) {
		this.episodeId = episodeId;
		this.patientName = patientLastName + " " + patientFirstName;
		this.placeName = placeName;
		this.triageCategory = new TriageCategoryBo(triageCategoryName, colorCode);
		this.topic = topic;
	}

}
