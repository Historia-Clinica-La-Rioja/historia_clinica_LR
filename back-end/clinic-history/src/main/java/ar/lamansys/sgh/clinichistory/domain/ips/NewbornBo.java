package ar.lamansys.sgh.clinichistory.domain.ips;

import ar.lamansys.sgh.clinichistory.domain.ips.enums.EBirthCondition;
import ar.lamansys.sgh.clinichistory.domain.ips.enums.EGender;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.entity.Newborn;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class NewbornBo {

	private Integer id;

	private Short weight;

	private EBirthCondition birthConditionType;

	private EGender genderId;

	private Integer obstetricEventId;

	public NewbornBo (Newborn newborn){
		this.id = newborn.getId();
		this.weight = newborn.getWeight();
		this.genderId = newborn.getGenderId() != null ? EGender.map(newborn.getGenderId()) : null;
		this.birthConditionType = newborn.getBirthConditionType() != null ? EBirthCondition.map(newborn.getBirthConditionType()) : null;
		this.obstetricEventId = newborn.getObstetricEventId();
	}

}
