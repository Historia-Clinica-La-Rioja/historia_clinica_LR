package net.pladema.person.controller.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class BMPersonDto extends APersonDto {

    private Integer id;
    
    private Short provinceId;
    
    private Short departmentId;

	private Short countryId;

}
