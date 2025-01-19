package net.pladema.medicalconsultation.diary.repository.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "diary_booking_restriction")
@Entity
public class DiaryBookingRestriction {

    @Id
    @Column(name = "diary_id", nullable = false)
    private Integer diaryId;

    @Column(name = "restriction_type", nullable = false)
    private Short restrictionType;

    @Column(name = "days", nullable = true)
    private Integer days;

}


