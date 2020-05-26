package net.pladema.internation.controller.internment.mapper;

import net.pladema.internation.controller.internment.dto.HealthCareProfessionalGroupDto;
import net.pladema.internation.repository.documents.entity.HealthcareProfessionalGroup;
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
