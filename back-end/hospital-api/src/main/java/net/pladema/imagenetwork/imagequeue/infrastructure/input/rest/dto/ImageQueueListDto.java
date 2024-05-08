package net.pladema.imagenetwork.imagequeue.infrastructure.input.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.ToString;
import lombok.Value;
import net.pladema.imagenetwork.imagequeue.domain.EImageMoveStatus;

import java.time.LocalDateTime;
import java.util.List;

@Value
@Builder
@ToString
@AllArgsConstructor
public class ImageQueueListDto {

    Integer id;
    Integer appointmentId;
    ImageQueuePatientDataDto patient;
    LocalDateTime updatedOn;
    Short modalityId;
    Integer equipmentId;
    EImageMoveStatus imageMoveStatus;
    String result;
    Integer serviceRequestId;
    List<String> studies;
    Integer transcribedServiceRequestId;

}
