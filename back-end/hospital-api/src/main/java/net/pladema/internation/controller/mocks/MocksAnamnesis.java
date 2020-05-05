package net.pladema.internation.controller.mocks;

import net.pladema.internation.controller.documents.anamnesis.dto.AnamnesisDto;
import net.pladema.internation.controller.ips.dto.AnthropometricDataDto;
import net.pladema.internation.controller.ips.dto.ClinicalObservationDto;
import net.pladema.internation.controller.ips.dto.VitalSignDto;


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

    private static VitalSignDto mockVitalSignDto(int id) {
        VitalSignDto vitalSignDto = new VitalSignDto();
        vitalSignDto.setBloodOxygenSaturation(mockNewClinicalObservationDto(id + 1));
        vitalSignDto.setDiastolicBloodPressure(mockNewClinicalObservationDto(id + 2));
        vitalSignDto.setSystolicBloodPressure(mockNewClinicalObservationDto(id + 2));
        vitalSignDto.setHeartRate(mockNewClinicalObservationDto(id + 3));
        vitalSignDto.setRespiratoryRate(mockNewClinicalObservationDto(id + 4));
        vitalSignDto.setTemperature(mockNewClinicalObservationDto(id + 5));
        return vitalSignDto;
    }

    private static AnthropometricDataDto mockAnthropometricDataDto(int id) {
        AnthropometricDataDto anthropometricDataDto = new AnthropometricDataDto();
        anthropometricDataDto.setHeight(mockNewClinicalObservationDto(id + 101));
        anthropometricDataDto.setWeight(mockNewClinicalObservationDto(id + 102));
        anthropometricDataDto.setBloodType(mockNewClinicalObservationDto(id + 103));
        return anthropometricDataDto;
    }

    private static ClinicalObservationDto mockNewClinicalObservationDto(int i) {
        ClinicalObservationDto result = new ClinicalObservationDto();
        result.setId(i);
        result.setValue("Value123/%");
        return result;
    }
}
