package net.pladema.internation.controller.mocks;

import net.pladema.internation.controller.dto.core.AnamnesisDto;
import net.pladema.internation.controller.dto.ips.AnthropometricDataDto;
import net.pladema.internation.controller.dto.ips.ClinicalObservationDto;
import net.pladema.internation.controller.dto.ips.VitalSignDto;


public class MocksAnamnesis {

    private MocksAnamnesis(){
        super();
    }

    public static AnamnesisDto mockAnamnesisDto(Integer id){
        AnamnesisDto result = new AnamnesisDto();
        result.setVitalSigns(mockVitalSignDto(id));
        result.setAnthropometricData(mockAnthropometricDataDto(id));
        return result;
    }

    private static VitalSignDto mockVitalSignDto(int i) {
        VitalSignDto result = new VitalSignDto();
        result.setBloodOxygenSaturation(mockNewClinicalObservationDto(i+1));
        result.setDiastolicBloodPressure(mockNewClinicalObservationDto(i+2));
        result.setSystolicBloodPressure(mockNewClinicalObservationDto(i+2));
        result.setHeartRate(mockNewClinicalObservationDto(i+3));
        result.setRespiratoryRate(mockNewClinicalObservationDto(i+4));
        result.setTemperature(mockNewClinicalObservationDto(i+5));
        result.setMeanPressure(mockNewClinicalObservationDto(i+6));
        return result;
    }

    private static AnthropometricDataDto mockAnthropometricDataDto(int i) {
        AnthropometricDataDto result = new AnthropometricDataDto();
        result.setHeight(mockNewClinicalObservationDto(i+101));
        result.setWeight(mockNewClinicalObservationDto(i+102));
        result.setBloodType(mockNewClinicalObservationDto(i+103));
        return result;
    }

    private static ClinicalObservationDto mockNewClinicalObservationDto(int i) {
        ClinicalObservationDto result = new ClinicalObservationDto();
        result.setId(i);
        result.setDeleted(false);
        result.setValue("Value123/%");
        return result;
    }
}
