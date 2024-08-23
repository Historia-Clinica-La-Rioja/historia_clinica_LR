package net.pladema.establishment.domain.bed;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class EmergencyCareBedBo {

	private Integer id;
	private String description;
	private boolean isAvailable;
}
