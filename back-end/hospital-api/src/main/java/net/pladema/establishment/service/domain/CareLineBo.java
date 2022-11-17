package net.pladema.establishment.service.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class CareLineBo {

    private Integer id;

    private String description;

    private List<ClinicalSpecialtyBo> clinicalSpecialties;

    public CareLineBo (Integer id, String description) {
    	this.id = id;
    	this.description = description;
	}
}