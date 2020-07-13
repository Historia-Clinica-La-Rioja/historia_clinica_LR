package net.pladema.appointment.repository.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import net.pladema.clinichistory.hospitalization.repository.listener.InternationAuditableEntity;
import net.pladema.clinichistory.hospitalization.repository.listener.InternationListener;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDate;

@Entity
@Table(name = "diary")
@EntityListeners(InternationListener.class)
@Getter
@Setter
@ToString
public class Diary extends InternationAuditableEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "healthcare_professional_id", nullable = false)
    private Integer healthcareProfessionalId;

    @Column(name = "doctors_office_id", nullable = false)
    private Integer doctorsOfficeId;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Column(name = "shift_duration", nullable = false)
    private Short shiftDuration;

    @Column(name = "automatic_renewal", columnDefinition = "boolean default false", nullable = false)
    private Boolean automaticRenewal;

    @Column(name = "days_before_renew", columnDefinition = "smallint default 0", nullable = false)
    private Short daysBeforeRenew;

    @Column(name = "professional_asign_shift", columnDefinition = "boolean default false", nullable = false)
    private Boolean professionalAsignShift;

    @Column(name = "include_holiday", columnDefinition = "boolean default false", nullable = false)
    private Boolean includeHoliday;

    @Column(name = "active", columnDefinition = "boolean default true", nullable = false)
    private Boolean active;

}
