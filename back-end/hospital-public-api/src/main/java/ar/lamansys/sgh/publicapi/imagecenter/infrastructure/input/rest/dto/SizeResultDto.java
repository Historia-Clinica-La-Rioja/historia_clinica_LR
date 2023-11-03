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
public class SizeResultDto implements Serializable {
	private Integer idMove;
	private Integer size;
	private String imageId;

	@JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
	public SizeResultDto(@JsonProperty("idMove") Integer idMove,
						 @JsonProperty("size") Integer size,
						 @JsonProperty("imageId") String imageId
	) {
		this.idMove = idMove;
		this.size = size;
		this.imageId = imageId;

	}
}
