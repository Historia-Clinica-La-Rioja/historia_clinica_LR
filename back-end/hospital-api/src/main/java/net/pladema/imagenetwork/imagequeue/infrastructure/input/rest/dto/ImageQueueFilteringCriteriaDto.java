package net.pladema.imagenetwork.imagequeue.infrastructure.input.rest.dto;

import ar.lamansys.sgx.shared.dates.controller.dto.DateDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.pladema.imagenetwork.imagequeue.domain.EImageMoveStatus;

import javax.validation.constraints.NotNull;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
public class ImageQueueFilteringCriteriaDto {

    @NotNull
    private DateDto from;
    @NotNull
    private DateDto to;
    private Integer equipmentId;
    private Short modalityId;
    @NotNull
    private List<EImageMoveStatus> statusList;
    private String name;
    private String identificationNumber;
    private String study;

}
