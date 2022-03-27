package net.pladema.medicalconsultation.appointment.controller.dto;

import ar.lamansys.sgh.shared.infrastructure.input.service.BasicDataPersonDto;
import lombok.Getter;
import lombok.Setter;
import net.pladema.medicalconsultation.appointment.service.domain.AttentionTypeReportItemBo;

import java.time.LocalTime;

@Getter
public class AttentionTypeReportItemDto {

    private final LocalTime hour;

    private final Integer patientId;

    private final String firstName;

    private final String middleNames;

    private final String lastName;

    private final String otherLastNames;

    private final String identificationType;

    private final String identificationNumber;

    private final Integer patientMedicalCoverageId;

    @Setter
    private String medicalCoverageName;

    @Setter
    private String medicalCoverageAffiliateNumber;

    private final String appointmentState;

    public AttentionTypeReportItemDto(AttentionTypeReportItemBo appointment, BasicDataPersonDto person){
        this.hour = appointment.getHour();
        this.patientId = appointment.getPatientId();
        this.firstName = person.getFirstName();
        this.middleNames = person.getMiddleNames();
        this.lastName = person.getLastName();
        this.otherLastNames = person.getOtherLastNames();
        this.identificationType = person.getIdentificationType();
        this.identificationNumber = person.getIdentificationNumber();
        this.patientMedicalCoverageId = appointment.getPatientMedicalCoverageId();
        this.appointmentState = appointment.getAppointmentState();
        this.medicalCoverageName = null;
        this.medicalCoverageAffiliateNumber = null;
    }
}
