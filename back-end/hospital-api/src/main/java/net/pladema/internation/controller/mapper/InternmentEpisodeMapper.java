package net.pladema.internation.controller.mapper;

import net.pladema.internation.controller.dto.InternmentSummaryDto;
import net.pladema.internation.controller.dto.internmentpatient.InternmentPatientDto;
import net.pladema.internation.repository.core.domain.InternmentSummary;
import net.pladema.internation.service.domain.internment.BasicListedPatientBo;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper
public interface InternmentEpisodeMapper {


    @Named("toInternmentSummaryDto")
    @Mapping(target = "bed.id", source = "bedId")
    @Mapping(target = "bed.bedNumber", source = "bedNumber")
    @Mapping(target = "bed.room.id", source = "bedId")
    @Mapping(target = "bed.room.roomNumber", source = "roomNumber")
    @Mapping(target = "doctor.id", source = "healthcareProfessionalId")
    @Mapping(target = "doctor.firstName", source = "firstName")
    @Mapping(target = "doctor.lastName", source = "lastName")
    @Mapping(target = "specialty.id", source = "clinicalSpecialtyId")
    @Mapping(target = "specialty.name", source = "specialty")
    InternmentSummaryDto toInternmentSummaryDto(InternmentSummary internmentSummary);


    @Named("toInternmentPatientDto")
    InternmentPatientDto toInternmentPatientDto(BasicListedPatientBo basicListedPatientBo);

    @Named("toListInternmentPatientDto")
    @IterableMapping(qualifiedByName = "toInternmentPatientDto")
    List<InternmentPatientDto> toListInternmentPatientDto(List<BasicListedPatientBo> basicListedPatientBos);
}
