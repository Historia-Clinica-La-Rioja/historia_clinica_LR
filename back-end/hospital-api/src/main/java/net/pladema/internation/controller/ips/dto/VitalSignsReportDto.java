package net.pladema.internation.controller.ips.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.annotation.Nullable;
import javax.validation.Valid;
import java.io.Serializable;

@Getter
@Setter
@ToString
public class VitalSignsReportDto implements Serializable {

    @Valid
    @Nullable
    private ReportClinicalObservationDto systolicBloodPressure;

    @Valid
    @Nullable
    private ReportClinicalObservationDto diastolicBloodPressure;

    @Valid
    @Nullable
    private ReportClinicalObservationDto temperature;

    @Valid
    @Nullable
    private ReportClinicalObservationDto heartRate;

    @Valid
    @Nullable
    private ReportClinicalObservationDto respiratoryRate;

    @Valid
    @Nullable
    private ReportClinicalObservationDto bloodOxygenSaturation;

}
