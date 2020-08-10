package net.pladema.establishment.controller.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Getter
@Setter
@ToString
public class PatientBedRelocationDto implements Serializable {

	private static final long serialVersionUID = 3940040590318704813L;

	private Integer originBedId;

	private Integer destinationBedId;

	private Integer internmentEpisodeId;

	private Boolean originBedFree = true;

	@NotNull
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
	private String relocationDate;
}
