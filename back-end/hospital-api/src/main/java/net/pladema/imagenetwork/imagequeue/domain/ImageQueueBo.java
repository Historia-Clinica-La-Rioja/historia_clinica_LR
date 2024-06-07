package net.pladema.imagenetwork.imagequeue.domain;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Getter
public class ImageQueueBo {

    private final Integer id;
    private final Integer appointmentId;
    private final Integer patientId;
    private final LocalDateTime createdOn;
    private final Integer modalityId;
    private final Integer equipmentId;
    private final EImageMoveStatus imageMoveStatus;
    private final Integer serviceRequestId;
    private final Integer studyId;
    private final Integer transcribedServiceRequestId;
    @Setter
    private List<String> studies;
    @Setter
    private ImageQueuePatientBo patient;

    public ImageQueueBo(Integer id, Integer appointmentId, Date createdOn, Integer patientId, Integer modalityId, Integer equipmentId,
                        Integer serviceRequestId, Integer studyId, Integer transcribedServiceRequestId, String status) {
        this.id = id;
        this.appointmentId = appointmentId;
        this.createdOn = LocalDateTime.ofInstant(createdOn.toInstant(), ZoneId.systemDefault());
        this.patientId = patientId;
        this.modalityId = modalityId;
        this.equipmentId = equipmentId;
        this.serviceRequestId = serviceRequestId;
        this.studyId = studyId;
        this.transcribedServiceRequestId = transcribedServiceRequestId;
        this.imageMoveStatus = EImageMoveStatus.map(status);
    }

    public String getPatientFirstName() {
        return Objects.isNull(getPatient()) ? null : getPatient().getFirstName();
    }

    public String getPatientLastName() {
        return Objects.isNull(getPatient()) ? null : getPatient().getLastName();
    }

    public String getPatientMiddleNames() {
        return Objects.isNull(getPatient()) ? null : getPatient().getMiddleNames();
    }

    public String getNameSelfDetermination() {
        return Objects.isNull(getPatient()) ? null : getPatient().getNameSelfDetermination();
    }

    public String getOtherLastNames() {
        return Objects.isNull(getPatient()) ? null : getPatient().getOtherLastNames();
    }

    public String getPatientIdentificationNumber() {
        return Objects.isNull(getPatient()) ? null : getPatient().getIdentificationNumber();
    }
}
