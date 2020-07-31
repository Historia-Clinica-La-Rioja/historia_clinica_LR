package net.pladema.establishment.repository.domain;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import net.pladema.establishment.repository.entity.BedCategory;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class BedCategoryVo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1053820325768716531L;
	
	Short id;
	String description;
	
	public BedCategoryVo(BedCategory bedCategory) {
		id = bedCategory.getId();
		description = bedCategory.getDescription();
	}
	
}
