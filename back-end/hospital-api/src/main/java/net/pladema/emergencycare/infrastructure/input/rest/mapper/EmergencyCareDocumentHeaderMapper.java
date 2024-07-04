package net.pladema.emergencycare.infrastructure.input.rest.mapper;

import ar.lamansys.sgx.shared.dates.configuration.LocalDateMapper;
import net.pladema.emergencycare.domain.EmergencyCareDocumentHeaderBo;
import net.pladema.emergencycare.infrastructure.input.rest.dto.EmergencyCareDocumentHeaderDto;
import net.pladema.establishment.controller.mapper.BedMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(uses = {LocalDateMapper.class, BedMapper.class})
public interface EmergencyCareDocumentHeaderMapper {

    @Named("toEmergencyCareDocumentHeaderDto")
    @Mapping(target = "id", source = "documentId")
    @Mapping(target = "bed", source = "bed", qualifiedByName = "toBedDtoFromBedBo")
    EmergencyCareDocumentHeaderDto toEmergencyCareDocumentHeaderDto(EmergencyCareDocumentHeaderBo emergencyCareDocumentHeaderBo);
}
