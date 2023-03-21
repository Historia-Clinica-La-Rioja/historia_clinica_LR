package net.pladema.medicalconsultation.equipmentdiary.repository.entity;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import java.io.Serializable;

@Getter
@Setter
@ToString
@Embeddable
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class EquipmentDiaryOpeningHoursPK implements Serializable {

    @Column(name = "equipment_diary_id", nullable = false)
    private Integer equipmentDiaryId;

    @Column(name = "opening_hours_id", nullable = false)
    private Integer openingHoursId;
}
