package net.pladema.emergencycare.controller.mapper;

import net.pladema.emergencycare.service.domain.enums.EEmergencyCareType;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

@Mapper
public interface EEmergencyCareTypeMapper {

    @Named("fromEmergencyCareTypeId")
    default EEmergencyCareType fromEmergencyCareTypeId(Short id) {
        return EEmergencyCareType.getById(id);
    }

}
