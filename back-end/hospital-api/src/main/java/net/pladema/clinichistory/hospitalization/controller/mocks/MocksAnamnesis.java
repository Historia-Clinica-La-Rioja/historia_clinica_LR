package net.pladema.clinichistory.hospitalization.controller.mocks;

import net.pladema.clinichistory.hospitalization.controller.documents.anamnesis.dto.AnamnesisDto;
import net.pladema.clinichistory.hospitalization.controller.generalstate.dto.AnthropometricDataDto;
import net.pladema.clinichistory.hospitalization.controller.generalstate.dto.ClinicalObservationDto;
import net.pladema.clinichistory.hospitalization.controller.generalstate.dto.EffectiveClinicalObservationDto;
import net.pladema.clinichistory.hospitalization.controller.generalstate.dto.VitalSignDto;


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
        vitalSignDto.setBloodOxygenSaturation(mockNewCompleteClinicalObservationDto(id + 1));
        vitalSignDto.setDiastolicBloodPressure(mockNewCompleteClinicalObservationDto(id + 2));
        vitalSignDto.setSystolicBloodPressure(mockNewCompleteClinicalObservationDto(id + 2));
        vitalSignDto.setHeartRate(mockNewCompleteClinicalObservationDto(id + 3));
        vitalSignDto.setRespiratoryRate(mockNewCompleteClinicalObservationDto(id + 4));
        vitalSignDto.setTemperature(mockNewCompleteClinicalObservationDto(id + 5));
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

    private static EffectiveClinicalObservationDto mockNewCompleteClinicalObservationDto(int i) {
        EffectiveClinicalObservationDto result = new EffectiveClinicalObservationDto();
        result.setId(i);
        result.setValue("Value123/%");
        result.setEffectiveTime("2020-05-15'T'09:47:20.233'Z'");
        return result;
    }
}
