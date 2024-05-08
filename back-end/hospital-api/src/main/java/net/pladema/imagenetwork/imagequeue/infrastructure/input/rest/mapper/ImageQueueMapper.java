package net.pladema.imagenetwork.imagequeue.infrastructure.input.rest.mapper;

import net.pladema.imagenetwork.imagequeue.domain.ImageQueueBo;
import net.pladema.imagenetwork.imagequeue.domain.ImageQueuePatientBo;
import net.pladema.imagenetwork.imagequeue.infrastructure.input.rest.dto.ImageQueueListDto;
import net.pladema.imagenetwork.imagequeue.infrastructure.input.rest.dto.ImageQueuePatientDataDto;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

import java.util.List;

@Mapper
public interface ImageQueueMapper {
    @Named("toImageQueueListDto")
    ImageQueueListDto toImageQueueListDto(ImageQueueBo imageQueueBo);

    @Named("toImageQueueListDto")
    @IterableMapping(qualifiedByName = "toImageQueueListDto")
    List<ImageQueueListDto> toImageQueueListDto(List<ImageQueueBo> imageQueueBo);

    @Named("toImageQueueBasicPatientDto")
    ImageQueuePatientDataDto toImageQueueBasicPatientDto(ImageQueuePatientBo patientData);

}
