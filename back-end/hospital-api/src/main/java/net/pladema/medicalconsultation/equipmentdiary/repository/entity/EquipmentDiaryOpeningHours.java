package net.pladema.medicalconsultation.equipmentdiary.repository.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "equipment_diary_opening_hours")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class EquipmentDiaryOpeningHours implements Serializable {

    @EmbeddedId
    private EquipmentDiaryOpeningHoursPK pk;

    @Column(name = "medical_attention_type_id", nullable = false)
    private Short medicalAttentionTypeId;

    @Column(name = "overturn_count", columnDefinition = "smallint default 0", nullable = false)
    private Short overturnCount;

    @Column(name = "external_appointments_allowed")
    private Boolean externalAppointmentsAllowed;

    public EquipmentDiaryOpeningHours(
            Integer equipmentDiaryId,
            Integer openingHoursId,
            Short medicalAttentionTypeId,
            Short overturnCount,
            Boolean externalAppointmentsAllowed
    ) {
        this.pk = new EquipmentDiaryOpeningHoursPK(equipmentDiaryId, openingHoursId);
        this.medicalAttentionTypeId = medicalAttentionTypeId;
        this.overturnCount = overturnCount;
        this.externalAppointmentsAllowed = externalAppointmentsAllowed;
    }

    public void plusOverTurnCount() {
        this.overturnCount++;
    }
}
