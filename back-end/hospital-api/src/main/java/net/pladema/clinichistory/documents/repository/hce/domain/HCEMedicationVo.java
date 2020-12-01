package net.pladema.clinichistory.documents.repository.hce.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.pladema.clinichistory.documents.repository.ips.masterdata.entity.Snomed;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class HCEMedicationVo extends HCEClinicalTermVo {

    public HCEMedicationVo(Integer id, Snomed snomed, String statusId) {
        super(id, snomed, statusId);
    }
}