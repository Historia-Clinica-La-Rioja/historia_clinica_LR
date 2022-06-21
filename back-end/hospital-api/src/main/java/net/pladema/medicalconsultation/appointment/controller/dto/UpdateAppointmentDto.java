package net.pladema.medicalconsultation.appointment.controller.dto;

import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.annotation.Nullable;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class UpdateAppointmentDto {

    @NotNull
    private Integer appointmentId;

    @NotNull
    private Integer patientId;

    @NotNull
    private boolean isOverturn = false;

    @NotNull
    private short appointmentStateId;

    @Nullable
    Integer patientMedicalCoverageId;

    @Nullable
    @Length(max = 20, message = "{appointment.new.phoneNumber.invalid}")
    private String phoneNumber;
}
