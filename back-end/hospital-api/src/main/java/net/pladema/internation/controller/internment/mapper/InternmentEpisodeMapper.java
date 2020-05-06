package net.pladema.internation.controller.internment.mapper;

import net.pladema.internation.controller.internment.dto.InternmentEpisodeDto;
import net.pladema.internation.controller.internment.dto.InternmentSummaryDto;
import net.pladema.internation.controller.internment.dto.InternmentPatientDto;
import net.pladema.internation.repository.core.domain.InternmentSummaryVo;
import net.pladema.internation.service.internment.domain.BasicListedPatientBo;
import net.pladema.internation.service.internment.domain.InternmentEpisodeBo;
import org.mapstruct.AfterMapping;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
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
    InternmentSummaryDto toInternmentSummaryDto(InternmentSummaryVo internmentSummaryVo);

    @Named("toInternmentPatientDto")
    InternmentPatientDto toInternmentPatientDto(BasicListedPatientBo basicListedPatientBo);

    @Named("toListInternmentPatientDto")
    @IterableMapping(qualifiedByName = "toInternmentPatientDto")
    List<InternmentPatientDto> toListInternmentPatientDto(List<BasicListedPatientBo> basicListedPatientBos);

    @Named("toInternmentEpisodeDto")
    InternmentEpisodeDto toInternmentEpisodeDto(InternmentEpisodeBo internmentEpisode);

    @Named("toListInternmentEpisodeDto")
    @IterableMapping(qualifiedByName = "toInternmentEpisodeDto")
    List<InternmentEpisodeDto> toListInternmentEpisodeDto(List<InternmentEpisodeBo> internmentEpisodes);

    @AfterMapping
    default void configDocumentsSummary(@MappingTarget InternmentSummaryDto target, InternmentSummaryVo source){
        if(target.getDocuments().getAnamnesis().getId() == null)
            target.getDocuments().setAnamnesis(null);
        if(target.getDocuments().getEpicrisis().getId() == null)
            target.getDocuments().setEpicrisis(null);
    }
}
