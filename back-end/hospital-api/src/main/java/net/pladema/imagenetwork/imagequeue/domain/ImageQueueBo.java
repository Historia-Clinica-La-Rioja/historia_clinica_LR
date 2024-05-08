package net.pladema.imagenetwork.imagequeue.domain;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@Getter
public class ImageQueueBo {

    private final Integer id;
    private final Integer appointmentId;
    private final Integer patientId;
    private final LocalDateTime updatedOn;
    private final Short modalityId;
    private final Integer equipmentId;
    private final EImageMoveStatus imageMoveStatus;
    private final Integer serviceRequestId;
    private final Integer studyId;
    private final Integer transcribedServiceRequestId;
    @Setter
    private List<String> studies;
    @Setter
    private ImageQueuePatientBo patient;

    public ImageQueueBo(Integer id, Integer appointmentId, Date updatedOn, Integer patientId, Short modalityId, Integer equipmentId,
                        Integer serviceRequestId, Integer studyId, Integer transcribedServiceRequestId, String status) {
        this.id = id;
        this.appointmentId = appointmentId;
        this.updatedOn = LocalDateTime.ofInstant(updatedOn.toInstant(), ZoneId.systemDefault());
        this.patientId = patientId;
        this.modalityId = modalityId;
        this.equipmentId = equipmentId;
        this.serviceRequestId = serviceRequestId;
        this.studyId = studyId;
        this.transcribedServiceRequestId = transcribedServiceRequestId;
        this.imageMoveStatus = EImageMoveStatus.map(status);
    }

}
