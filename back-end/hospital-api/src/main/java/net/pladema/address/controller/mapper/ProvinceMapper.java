package net.pladema.address.controller.mapper;

import net.pladema.address.controller.dto.ProvinceDto;
import net.pladema.address.repository.entity.Province;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

@Mapper
public interface ProvinceMapper {

    @Named("fromProvince")
    public ProvinceDto fromProvince(Province province);
}
