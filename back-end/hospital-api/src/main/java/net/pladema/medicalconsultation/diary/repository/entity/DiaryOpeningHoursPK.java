package net.pladema.medicalconsultation.diary.repository.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Getter
@Setter
@ToString
@Embeddable
@EqualsAndHashCode
public class DiaryOpeningHoursPK implements Serializable {

    @Column(name = "diary_id", nullable = false)
    private Integer diaryId;

    @Column(name = "opening_hours_id", nullable = false)
    private Integer openingHoursId;
}
