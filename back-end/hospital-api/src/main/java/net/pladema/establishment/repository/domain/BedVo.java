package net.pladema.establishment.repository.domain;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import net.pladema.establishment.repository.entity.Bed;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class BedVo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6951186729569872968L;

	Integer id;
	String bedNumber;
	Boolean free;

	public BedVo(Bed bed) {
		id = bed.getId();
		bedNumber = bed.getBedNumber();
		free = bed.getFree();
	}

}
