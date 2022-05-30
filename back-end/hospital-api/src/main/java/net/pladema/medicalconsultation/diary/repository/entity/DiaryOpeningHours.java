package net.pladema.medicalconsultation.diary.repository.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "diary_opening_hours")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class DiaryOpeningHours implements Serializable {

    @EmbeddedId
    private DiaryOpeningHoursPK pk;

    @Column(name = "medical_attention_type_id", nullable = false)
    private Short medicalAttentionTypeId;

    @Column(name = "overturn_count", columnDefinition = "smallint default 0", nullable = false)
    private Short overturnCount;

    @Column(name = "external_appointments_allowed")
    private Boolean externalAppointmentsAllowed;

    public DiaryOpeningHours(
            Integer diaryId,
            Integer openingHoursId,
            Short medicalAttentionTypeId,
            Short overturnCount,
            Boolean externalAppointmentsAllowed
    ) {
        this.pk = new DiaryOpeningHoursPK(diaryId, openingHoursId);
        this.medicalAttentionTypeId = medicalAttentionTypeId;
        this.overturnCount = overturnCount;
        this.externalAppointmentsAllowed = externalAppointmentsAllowed;
    }

    public void plusOverTurnCount() {
        this.overturnCount++;
    }
}
