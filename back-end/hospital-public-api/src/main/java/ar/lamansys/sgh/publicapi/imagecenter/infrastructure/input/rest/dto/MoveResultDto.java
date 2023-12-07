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
public class MoveResultDto implements Serializable {
	private Integer idMove;
	private String result;
	private String status;

	@JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
	public MoveResultDto(@JsonProperty("idMove") Integer idMove,
			@JsonProperty("result") String result,
			@JsonProperty("status") String status
	) {
		this.idMove = idMove;
		this.result = result;
		this.status = status;

	}
}
