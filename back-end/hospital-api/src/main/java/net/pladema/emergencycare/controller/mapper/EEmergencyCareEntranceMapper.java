package net.pladema.emergencycare.controller.mapper;

import net.pladema.emergencycare.service.domain.enums.EEmergencyCareEntrance;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

@Mapper
public interface EEmergencyCareEntranceMapper {

    @Named("fromEmergencyCareEntranceId")
    default EEmergencyCareEntrance fromEmergencyCareEntranceId(Short id) {
        return EEmergencyCareEntrance.getById(id);
    }

}
