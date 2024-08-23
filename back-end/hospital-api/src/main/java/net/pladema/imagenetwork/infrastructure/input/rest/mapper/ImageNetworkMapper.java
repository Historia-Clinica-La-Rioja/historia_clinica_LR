package net.pladema.imagenetwork.infrastructure.input.rest.mapper;

import ar.lamansys.sgx.shared.dates.configuration.LocalDateMapper;
import net.pladema.imagenetwork.domain.ErrorDownloadStudyBo;
import net.pladema.imagenetwork.domain.PacsListBo;
import net.pladema.imagenetwork.domain.StudyFileInfoBo;
import net.pladema.imagenetwork.domain.ViewerUrlBo;
import net.pladema.imagenetwork.infrastructure.input.rest.dto.ErrorDownloadStudyDto;
import net.pladema.imagenetwork.infrastructure.input.rest.dto.PacsListDto;
import net.pladema.imagenetwork.infrastructure.input.rest.dto.StudyFileInfoDto;
import net.pladema.imagenetwork.infrastructure.input.rest.dto.ViewerUrlDto;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.net.URI;
import java.util.List;
import java.util.Set;

@Mapper(uses = {LocalDateMapper.class})
public interface ImageNetworkMapper {

    @Named("uriToString")
    default String uriToString(URI uri) {
        return uri.toString();
    }

    @Named("fromSetURI")
    @IterableMapping(qualifiedByName = "uriToString")
    List<String> fromSetURI(Set<URI> urls);

    @Named("toPacsUrlDto")
    @Mapping(source = "urls", target = "urls", qualifiedByName = "fromSetURI")
    PacsListDto toPacsUrlDto(PacsListBo pacsListBo);

    @Named("toStudyFileInfoDto")
    StudyFileInfoDto toStudyFileInfoDto(StudyFileInfoBo studyFileInfoBo);

    @Named("toStudyFileInfoDto")
    ViewerUrlDto toViewerUrlDto(ViewerUrlBo viewerUrlBo);

    @Named("toErrorDownloadStudyBo")
    ErrorDownloadStudyBo toErrorDownloadStudyBo(ErrorDownloadStudyDto errorDownloadStudyDto);
}
