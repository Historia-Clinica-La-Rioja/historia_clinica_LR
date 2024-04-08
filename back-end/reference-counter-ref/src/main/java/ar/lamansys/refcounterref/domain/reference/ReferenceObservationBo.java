package ar.lamansys.refcounterref.domain.reference;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class ReferenceObservationBo {

	private Integer personId;

	private String createdBy;

	private String observation;

	private LocalDateTime date;

	public ReferenceObservationBo(Integer personId, String observation, LocalDateTime date) {
		this.personId = personId;
		this.observation = observation;
		this.date = date;
	}
}
