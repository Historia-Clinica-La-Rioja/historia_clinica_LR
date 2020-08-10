package net.pladema.clinichistory.generalstate.repository.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.pladema.clinichistory.hospitalization.repository.generalstate.domain.ClinicalTermVo;
import net.pladema.clinichistory.ips.repository.masterdata.entity.Snomed;

import java.time.LocalDate;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class HCEHospitalizationVo extends ClinicalTermVo {

    private Boolean main;

    private Integer sourceId;

    private LocalDate entryDate;

    private LocalDate dischargeDate;

    private Integer patientId;

    public HCEHospitalizationVo(Integer id, Snomed snomed, String statusId, boolean main, Integer sourceId,
                                LocalDate startDate, LocalDate inactivationDate, Integer patientId) {
        super(id, snomed, statusId);
        this.main = main;
        this.sourceId = sourceId;
        this.entryDate = startDate;
        this.dischargeDate = inactivationDate;
        this.patientId = patientId;
    }
}
