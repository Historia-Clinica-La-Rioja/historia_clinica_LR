package net.pladema.clinichistory.hospitalization.controller.mapper;

import net.pladema.clinichistory.hospitalization.controller.dto.InternmentEpisodeADto;
import net.pladema.clinichistory.hospitalization.controller.dto.InternmentEpisodeDto;
import net.pladema.clinichistory.hospitalization.controller.dto.InternmentPatientDto;
import net.pladema.clinichistory.hospitalization.controller.dto.InternmentSummaryDto;
import net.pladema.clinichistory.hospitalization.controller.dto.summary.InternmentEpisodeBMDto;
import net.pladema.clinichistory.hospitalization.repository.domain.InternmentEpisode;
import net.pladema.clinichistory.hospitalization.service.domain.BasicListedPatientBo;
import net.pladema.clinichistory.hospitalization.service.domain.InternmentEpisodeBo;
import net.pladema.clinichistory.hospitalization.service.domain.InternmentSummaryBo;
import ar.lamansys.sgx.shared.dates.configuration.LocalDateMapper;
import org.mapstruct.*;

import java.util.List;

@Mapper(uses = {LocalDateMapper.class})
public interface InternmentEpisodeMapper {


    @Named("toInternmentSummaryDto")
    @Mapping(target = "bed.id", source = "bedId")
    @Mapping(target = "bed.bedNumber", source = "bedNumber")
    @Mapping(target = "bed.room.id", source = "bedId")
    @Mapping(target = "bed.room.roomNumber", source = "roomNumber")
    @Mapping(target = "bed.room.sector.description", source = "sectorDescription")
    InternmentSummaryDto toInternmentSummaryDto(InternmentSummaryBo internmentSummaryBo);

    @Named("toInternmentEpisode")
    InternmentEpisode toInternmentEpisode(InternmentEpisodeADto internmentEpisodeDto);

    @Named("toInternmentEpisodeDto")
    @Mapping(target = "patient.id", source = "patientId")
    @Mapping(target = "bed.id", source = "bedId")
    InternmentEpisodeDto toInternmentEpisodeDto(InternmentEpisode internmentEpisode);

    @Named("toInternmentPatientDto")
    InternmentPatientDto toInternmentPatientDto(BasicListedPatientBo basicListedPatientBo);

    @Named("toListInternmentPatientDto")
    @IterableMapping(qualifiedByName = "toInternmentPatientDto")
    List<InternmentPatientDto> toListInternmentPatientDto(List<BasicListedPatientBo> basicListedPatientBos);

    @Named("toInternmentEpisodeDto")
    @Mapping(target = "patient.fullName", expression = "java(patientBo.getFirstName() + \" \" + patientBo.getLastName())")
    InternmentEpisodeDto toInternmentEpisodeDto(InternmentEpisodeBo internmentEpisode);

    @Named("toListInternmentEpisodeDto")
    @IterableMapping(qualifiedByName = "toInternmentEpisodeDto")
    List<InternmentEpisodeDto> toListInternmentEpisodeDto(List<InternmentEpisodeBo> internmentEpisodes);

    @Named("toInternmentEpisodeBMDto")
    InternmentEpisodeBMDto toInternmentEpisodeBMDto (InternmentEpisode internmentEpisode);

    @AfterMapping
    default void configDocumentsSummary(@MappingTarget InternmentSummaryDto target, InternmentSummaryBo source){
        if(target.getDocuments().getAnamnesis().getId() == null)
            target.getDocuments().setAnamnesis(null);
        if(target.getDocuments().getEpicrisis().getId() == null)
            target.getDocuments().setEpicrisis(null);
    }
}
