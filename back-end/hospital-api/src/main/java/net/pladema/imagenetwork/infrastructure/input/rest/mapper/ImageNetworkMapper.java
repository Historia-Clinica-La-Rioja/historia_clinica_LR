package net.pladema.imagenetwork.infrastructure.input.rest.mapper;

import ar.lamansys.sgx.shared.dates.configuration.LocalDateMapper;
import net.pladema.imagenetwork.domain.ErrorDownloadStudyBo;
import net.pladema.imagenetwork.domain.PacsBo;
import net.pladema.imagenetwork.domain.PacsListBo;
import net.pladema.imagenetwork.domain.StudyFileInfoBo;
import net.pladema.imagenetwork.domain.ViewerUrlBo;
import net.pladema.imagenetwork.infrastructure.input.rest.dto.ErrorDownloadStudyDto;
import net.pladema.imagenetwork.infrastructure.input.rest.dto.PacsDto;
import net.pladema.imagenetwork.infrastructure.input.rest.dto.PacsListDto;
import net.pladema.imagenetwork.infrastructure.input.rest.dto.StudyFileInfoDto;
import net.pladema.imagenetwork.infrastructure.input.rest.dto.ViewerUrlDto;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;
import java.util.Set;

@Mapper(uses = {LocalDateMapper.class})
public interface ImageNetworkMapper {

    @Named("toPacsDto")
    @Mapping(target = "id", source = "id")
    @Mapping(target = "url", expression = "java(pacsBo.getUrl())")
    PacsDto toPacsDto(PacsBo pacsBo);

    @Named("toPacsDtoList")
    @IterableMapping(qualifiedByName = "toPacsDto")
    List<PacsDto> toPacsDtoList(Set<PacsBo> pacsBo);

    @Named("toPacsUrlDto")
    @Mapping(target = "pacs", source = "pacs", qualifiedByName = "toPacsDtoList")
    PacsListDto toPacsUrlDto(PacsListBo pacsListBo);

    @Named("toPacsBo")
    @Mapping(target = "domain", expression = "java(PacsBo.getDomain(pacsDto.getUrl()))")
    PacsBo toPacsBo(PacsDto pacsDto);

    @Named("toPacsBoList")
    @IterableMapping(qualifiedByName = "toPacsBo")
    Set<PacsBo> toPacsBoSet(List<PacsDto> pacsDto);

    @Named("toPacsUrlBo")
    @Mapping(target = "pacs", source = "pacs", qualifiedByName = "toPacsBoList")
    PacsListBo toPacsUrlBo(PacsListDto pacsListDto);

    @Named("toStudyFileInfoDto")
    StudyFileInfoDto toStudyFileInfoDto(StudyFileInfoBo studyFileInfoBo);

    @Named("toStudyFileInfoDto")
    ViewerUrlDto toViewerUrlDto(ViewerUrlBo viewerUrlBo);

    @Named("toErrorDownloadStudyBo")
    ErrorDownloadStudyBo toErrorDownloadStudyBo(ErrorDownloadStudyDto errorDownloadStudyDto);
}
