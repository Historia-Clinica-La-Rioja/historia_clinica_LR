package net.pladema.clinichistory.hospitalization.controller.mapper;

import net.pladema.clinichistory.hospitalization.controller.dto.HealthCareProfessionalGroupDto;
import net.pladema.clinichistory.hospitalization.repository.domain.HealthcareProfessionalGroup;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper
public interface HealthCareProfessionalGroupMapper {

    @Mapping(target = "internmentEpisodeId", source = "pk.internmentEpisodeId")
    @Mapping(target = "healthcareProfessionalId", source = "pk.healthcareProfessionalId")
    @Named("toHealthcareProfessionalGroupDto")
    HealthCareProfessionalGroupDto toHealthcareProfessionalGroupDto(HealthcareProfessionalGroup healthcareProfessionalGroup);
}
