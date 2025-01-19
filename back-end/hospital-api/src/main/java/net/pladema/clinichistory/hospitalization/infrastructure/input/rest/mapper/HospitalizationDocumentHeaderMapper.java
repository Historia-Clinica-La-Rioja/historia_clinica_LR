package net.pladema.clinichistory.hospitalization.infrastructure.input.rest.mapper;

import ar.lamansys.sgx.shared.dates.configuration.LocalDateMapper;
import net.pladema.clinichistory.hospitalization.domain.HospitalizationDocumentHeaderBo;
import net.pladema.clinichistory.hospitalization.infrastructure.input.rest.dto.HospitalizationDocumentHeaderDto;
import net.pladema.establishment.controller.mapper.BedMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(uses = {LocalDateMapper.class, BedMapper.class})
public interface HospitalizationDocumentHeaderMapper {

    @Named("toHospitalizationDocumentHeaderDto")
    @Mapping(target = "id", expression = "java(headerBo.getDocumentId())")
    @Mapping(target = "sourceTypeName", source = "sourceTypeName")
    @Mapping(target = "clinicalSpecialtyName", source = "clinicalSpecialtyName")
    @Mapping(target = "createdOn", source = "baseDocumentHeader.createdOn")
    @Mapping(target = "professionalName", source = "professionalName")
    @Mapping(target = "institutionName", source = "institutionName")
    @Mapping(target = "bed", source = "bed", qualifiedByName = "toBedDtoFromBedBo")
    HospitalizationDocumentHeaderDto toHospitalizationDocumentHeaderDto(HospitalizationDocumentHeaderBo headerBo);

}
