package net.pladema.snvs.infrastructure.input.rest.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class SnvsEventManualClassificationsDto {

    private SnvsEventDto snvsEvent;

    private List<ManualClassificationDto> manualClassifications;

}
