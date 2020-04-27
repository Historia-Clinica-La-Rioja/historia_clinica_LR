package net.pladema.internation.controller.mocks;

import net.pladema.internation.controller.dto.core.AnamnesisDto;
import net.pladema.internation.controller.dto.ips.AnthropometricDataDto;
import net.pladema.internation.controller.dto.ips.ClinicalObservationDto;
import net.pladema.internation.controller.dto.ips.VitalSignDto;

import java.util.ArrayList;
import java.util.List;


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

    private static List<VitalSignDto> mockVitalSignDto(int id) {
        List<VitalSignDto> result = new ArrayList<>();
        for (int i = 0; i<id;i++) {
            VitalSignDto vitalSignDto = new VitalSignDto();
            vitalSignDto.setBloodOxygenSaturation(mockNewClinicalObservationDto(i + 1));
            vitalSignDto.setDiastolicBloodPressure(mockNewClinicalObservationDto(i + 2));
            vitalSignDto.setSystolicBloodPressure(mockNewClinicalObservationDto(i + 2));
            vitalSignDto.setHeartRate(mockNewClinicalObservationDto(i + 3));
            vitalSignDto.setRespiratoryRate(mockNewClinicalObservationDto(i + 4));
            vitalSignDto.setTemperature(mockNewClinicalObservationDto(i + 5));
            vitalSignDto.setMeanPressure(mockNewClinicalObservationDto(i + 6));
            result.add(vitalSignDto);
        }
        return result;
    }

    private static List<AnthropometricDataDto> mockAnthropometricDataDto(int id) {
        List<AnthropometricDataDto> result = new ArrayList<>();
        for (int i = 0; i<id;i++) {
            AnthropometricDataDto anthropometricDataDto = new AnthropometricDataDto();
            anthropometricDataDto.setHeight(mockNewClinicalObservationDto(i + 101));
            anthropometricDataDto.setWeight(mockNewClinicalObservationDto(i + 102));
            anthropometricDataDto.setBloodType(mockNewClinicalObservationDto(i + 103));
        }
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
