package net.pladema.medicalconsultation.appointment.controller.dto;

import lombok.Getter;
import net.pladema.medicalconsultation.appointment.service.domain.AttentionTypeReportItemBo;
import net.pladema.person.controller.dto.BasicDataPersonDto;

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

    private final String healthInsuranceName;

    private final String medicalCoverageName;

    private final String medicalCoverageAffiliateNumber;

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
        this.healthInsuranceName = appointment.getHealthInsuranceName();
        this.medicalCoverageName = appointment.getMedicalCoverageName();
        this.medicalCoverageAffiliateNumber = appointment.getMedicalCoverageAffiliateNumber();
        this.appointmentState = appointment.getAppointmentState();
    }
}
