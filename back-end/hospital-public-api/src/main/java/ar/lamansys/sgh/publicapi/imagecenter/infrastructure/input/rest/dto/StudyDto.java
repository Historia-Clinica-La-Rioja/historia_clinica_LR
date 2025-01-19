package ar.lamansys.sgh.publicapi.imagecenter.infrastructure.input.rest.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@Setter
@ToString
@Builder
public class StudyDto implements Serializable {

	private String patientId;
	private String patientName;
	private String studyDate;
	private String studyTime;
	private String modality;
	private String studyInstanceUid;

	@JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
	public StudyDto(
			@JsonProperty("patientId") String patientId,
			@JsonProperty("patientName") String patientName,
			@JsonProperty("studyDate") String studyDate,
			@JsonProperty("studyTime") String studyTime,
			@JsonProperty("modality") String modality,
			@JsonProperty("studyInstanceUid") String studyInstanceUid) {

		this.patientId = patientId;
		this.patientName = patientName;
		this.studyDate = studyDate;
		this.studyTime = studyTime;
		this.modality = modality;
		this.studyInstanceUid = studyInstanceUid;
	}
}
