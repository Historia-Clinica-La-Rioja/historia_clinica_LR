package ar.lamansys.sgh.publicapi.imagecenter.infrastructure.input.rest.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@ToString
@Builder
public class PossibleStudiesDto implements Serializable {
	private Integer appointmentId;
	private Integer idMove;
	private List<StudyDto> studies;

	@JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
	public PossibleStudiesDto(
			@JsonProperty("appointmentId") Integer appointmentId,
			@JsonProperty("idMove") Integer idMove,
			@JsonProperty("studies")  List<StudyDto> studies
	) {
		this.idMove = idMove;
		this.appointmentId = appointmentId;
		this.studies = studies;

	}
}
