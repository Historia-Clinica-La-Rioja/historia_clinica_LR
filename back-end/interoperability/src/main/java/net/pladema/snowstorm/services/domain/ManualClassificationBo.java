package net.pladema.snowstorm.services.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.pladema.snowstorm.repository.entity.ManualClassification;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ManualClassificationBo {

	private Integer id;

	private String description;

	public ManualClassificationBo(ManualClassification manualClassification){
		this.id = manualClassification.getId();
		this.description = manualClassification.getDescription();
	}
}
