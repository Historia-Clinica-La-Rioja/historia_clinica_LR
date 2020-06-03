package net.pladema.person.controller.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.pladema.address.controller.dto.DepartmentDto;
import net.pladema.address.controller.dto.ProvinceDto;

@Getter
@Setter
@NoArgsConstructor
public class BMPersonDto extends APersonDto {

    private Integer id;
    
    private ProvinceDto province;
    
    private DepartmentDto department;
    
}
