package net.pladema.clinichistory.documents.service.hce.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.pladema.clinichistory.documents.repository.hce.domain.HCEHospitalizationVo;

import java.time.LocalDate;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class HCEHospitalizationBo extends HCEClinicalTermBo {

    private boolean main;

    private Integer sourceId;

    private LocalDate entryDate;

    private LocalDate dischargeDate;

    private Integer patientId;

    public HCEHospitalizationBo(HCEHospitalizationVo source){
        super(source.getId(), source.getSnomed(), source.getStatusId(), source.getStatus(), source.getPatientId());
        this.main = source.getMain();
        this.sourceId = source.getSourceId();
        this.entryDate = source.getEntryDate();
        this.dischargeDate = source.getDischargeDate();
        this.patientId = source.getPatientId();
    }

    public boolean isMain(){
        return main;
    }
}
