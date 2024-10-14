package net.pladema.imagenetwork.imagequeue.domain;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Getter
public class ImageQueueBo {

    private final Integer id;
    private final Integer appointmentId;
    private final Integer patientId;
    private final Integer modalityId;
    private final LocalDateTime lastTriedOn;
    private final LocalDateTime appointmentDateTime;
    private final Integer equipmentId;
    private final EImageMoveStatus imageMoveStatus;
    private final Integer serviceRequestId;
    private final Integer studyId;
    private final Integer transcribedServiceRequestId;
    private final String studyImageUID;
    @Setter
    private List<String> studies;
    @Setter
    private ImageQueuePatientBo patient;

    public ImageQueueBo(Integer id, Integer appointmentId, Integer patientId, Integer modalityId, Integer equipmentId,
                        Integer serviceRequestId, Integer studyId, Integer transcribedServiceRequestId, String status,
                        Date beginOfMove, Date endOfMove, LocalDate appointmentDate, LocalTime appointmentTime,
                        String studyImageUID) {
        this.id = id;
        this.appointmentId = appointmentId;
        this.lastTriedOn = buildLastTriedOn(beginOfMove,endOfMove);
        this.appointmentDateTime = LocalDateTime.of(appointmentDate,appointmentTime);
        this.patientId = patientId;
        this.modalityId = modalityId;
        this.equipmentId = equipmentId;
        this.serviceRequestId = serviceRequestId;
        this.studyId = studyId;
        this.transcribedServiceRequestId = transcribedServiceRequestId;
        this.imageMoveStatus = EImageMoveStatus.map(status);
        this.studyImageUID = studyImageUID;
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

    private LocalDateTime buildLastTriedOn(Date beginOfMove, Date endOfMove) {
        if (endOfMove != null && beginOfMove != null) {
            if (endOfMove.after(beginOfMove)) {
                return  LocalDateTime.ofInstant(endOfMove.toInstant(),ZoneId.systemDefault());
            }
            return LocalDateTime.ofInstant(beginOfMove.toInstant(),ZoneId.systemDefault());
        }
        if (endOfMove != null) {
            return LocalDateTime.ofInstant(endOfMove.toInstant(),ZoneId.systemDefault());
        }
        if (beginOfMove != null) {
            return LocalDateTime.ofInstant(beginOfMove.toInstant(),ZoneId.systemDefault());
        }
        return null;
    }
}
