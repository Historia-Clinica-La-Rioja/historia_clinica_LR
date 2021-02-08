package net.pladema.patient.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PatientPhotoDto {

    private Integer patientId;

    private String imageData;

    @Override
    public String toString() {
        return "PatientPhotoDto{" +
                "patientId" + patientId +
                "exists imageData='" + (imageData != null) + '\'' +
                '}';
    }
}
