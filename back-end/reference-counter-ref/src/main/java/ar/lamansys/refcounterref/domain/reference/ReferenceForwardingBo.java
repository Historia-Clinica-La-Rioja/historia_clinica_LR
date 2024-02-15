package ar.lamansys.refcounterref.domain.reference;

import ar.lamansys.refcounterref.domain.enums.EReferenceForwardingType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@ToString
@Getter
@Setter
@Builder
@AllArgsConstructor
public class ReferenceForwardingBo {

	private Integer id;

	private Integer personId;
	
	private Integer userId;

	private String createdBy;

	private String observation;

	private EReferenceForwardingType type;

	private LocalDateTime date;

	private Integer referenceId;

	public ReferenceForwardingBo(Integer id, Integer personId, Integer userId,
								 String observation, Short type, LocalDateTime date) {
		this.id = id;
		this.personId = personId;
		this.userId = userId;
		this.observation = observation;
		this.type = EReferenceForwardingType.getById(type);
		this.date = date;
	}

}
