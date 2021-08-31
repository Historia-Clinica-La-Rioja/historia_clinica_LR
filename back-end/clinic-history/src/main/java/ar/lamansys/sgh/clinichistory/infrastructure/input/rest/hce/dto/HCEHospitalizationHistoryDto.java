package ar.lamansys.sgh.clinichistory.infrastructure.input.rest.hce.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ar.lamansys.sgx.shared.dates.configuration.JacksonDateFormatConfig;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class HCEHospitalizationHistoryDto {

    private Integer sourceId;

    @JsonFormat(pattern = JacksonDateFormatConfig.DATE_FORMAT)
    private String entryDate;

    @JsonFormat(pattern = JacksonDateFormatConfig.DATE_FORMAT)
    private String dischargeDate;

    private HCEDiagnoseDto mainDiagnose;

    private List<HCEDiagnoseDto> alternativeDiagnoses;

    public HCEHospitalizationHistoryDto(int sourceId, String entryDate, String dischargeDate, List<HCEDiagnoseDto> mainDiagnose, List<HCEDiagnoseDto> alternativeDiagnoses) {
        this.sourceId = sourceId;
        this.entryDate = entryDate;
        this.dischargeDate = dischargeDate;
        this.mainDiagnose = mainDiagnose.get(0);
        this.alternativeDiagnoses = alternativeDiagnoses;
    }
}
