package ar.lamansys.sgh.clinichistory.infrastructure.input.rest.hce.dto;

import ar.lamansys.sgx.shared.dates.controller.dto.DateTimeDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class HCEHospitalizationHistoryDto {

    private Integer sourceId;

    private DateTimeDto entryDate;

    private DateTimeDto dischargeDate;

    private HCEDiagnoseDto mainDiagnose;

    private List<HCEDiagnoseDto> alternativeDiagnoses;

    public HCEHospitalizationHistoryDto(int sourceId, DateTimeDto entryDate, DateTimeDto dischargeDate, List<HCEDiagnoseDto> mainDiagnose, List<HCEDiagnoseDto> alternativeDiagnoses) {
        this.sourceId = sourceId;
        this.entryDate = entryDate;
        this.dischargeDate = dischargeDate;
        this.mainDiagnose = mainDiagnose.get(0);
        this.alternativeDiagnoses = alternativeDiagnoses;
    }
}
