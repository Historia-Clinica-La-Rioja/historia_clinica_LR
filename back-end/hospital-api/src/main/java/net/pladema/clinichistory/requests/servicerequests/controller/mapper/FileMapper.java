package net.pladema.clinichistory.requests.servicerequests.controller.mapper;

import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.mapper.SnomedMapper;
import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.dto.FileDto;
import ar.lamansys.sgh.clinichistory.domain.ips.FileBo;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

import java.util.List;

@Mapper(uses = {SnomedMapper.class})
public interface FileMapper {
    @Named("parseTo")
    FileDto parseTo(FileBo file);

    @Named("parseToList")
    @IterableMapping(qualifiedByName = "parseTo")
    List<FileDto> parseToList(List<FileBo> studyDtoList);
}
