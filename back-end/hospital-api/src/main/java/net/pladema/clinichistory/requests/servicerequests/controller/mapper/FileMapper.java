package net.pladema.clinichistory.requests.servicerequests.controller.mapper;

import net.pladema.clinichistory.hospitalization.controller.generalstate.mapper.SnomedMapper;
import net.pladema.clinichistory.requests.servicerequests.controller.dto.FileDto;
import net.pladema.clinichistory.requests.servicerequests.service.domain.FileBo;
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
