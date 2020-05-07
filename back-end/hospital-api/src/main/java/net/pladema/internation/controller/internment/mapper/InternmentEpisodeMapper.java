package net.pladema.internation.controller.internment.mapper;

import net.pladema.internation.controller.internment.dto.InternmentEpisodeADto;
import net.pladema.internation.controller.internment.dto.InternmentEpisodeDto;
import net.pladema.internation.controller.internment.dto.InternmentPatientDto;
import net.pladema.internation.controller.internment.dto.InternmentSummaryDto;
import net.pladema.internation.repository.core.domain.InternmentSummaryVo;
import net.pladema.internation.repository.core.entity.InternmentEpisode;
import net.pladema.internation.service.internment.domain.BasicListedPatientBo;
import net.pladema.internation.service.internment.domain.InternmentEpisodeBo;
import org.mapstruct.*;

import java.util.List;

@Mapper
public interface InternmentEpisodeMapper {


    @Named("toInternmentSummaryDto")
    @Mapping(target = "bed.id", source = "bedId")
    @Mapping(target = "bed.bedNumber", source = "bedNumber")
    @Mapping(target = "bed.room.id", source = "bedId")
    @Mapping(target = "bed.room.roomNumber", source = "roomNumber")
    @Mapping(target = "specialty.id", source = "clinicalSpecialtyId")
    @Mapping(target = "specialty.name", source = "specialty")
    InternmentSummaryDto toInternmentSummaryDto(InternmentSummaryVo internmentSummaryVo);

    @Named("toInternmentEpisode")
    InternmentEpisode toInternmentEpisode(InternmentEpisodeADto internmentEpisodeDto);

    @Named("toInternmentEpisodeDto")
    @Mapping(target = "patient.id", source = "patientId")
    @Mapping(target = "bed.id", source = "bedId")
    @Mapping(target = "specialty.id", source = "clinicalSpecialtyId")
    InternmentEpisodeDto toInternmentEpisodeDto(InternmentEpisode internmentEpisode);

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
