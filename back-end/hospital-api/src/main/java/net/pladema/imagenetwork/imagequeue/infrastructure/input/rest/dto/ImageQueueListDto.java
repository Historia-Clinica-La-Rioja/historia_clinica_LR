package net.pladema.imagenetwork.imagequeue.infrastructure.input.rest.dto;

import ar.lamansys.sgx.shared.dates.controller.dto.DateTimeDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.ToString;
import lombok.Value;
import net.pladema.imagenetwork.imagequeue.domain.EImageMoveStatus;

import java.util.List;

@Value
@Builder
@ToString
@AllArgsConstructor
public class ImageQueueListDto {

    Integer id;
    Integer appointmentId;
    ImageQueuePatientDataDto patient;
    DateTimeDto createdOn;
    Short modalityId;
    Integer equipmentId;
    EImageMoveStatus imageMoveStatus;
    String result;
    Integer serviceRequestId;
    List<String> studies;
    Integer transcribedServiceRequestId;

}
