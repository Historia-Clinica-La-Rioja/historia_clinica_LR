package net.pladema.internation.repository.ips.generalstate;

import lombok.*;
import net.pladema.internation.repository.masterdata.entity.Snomed;

import java.time.LocalDate;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AllergyConditionVo extends ClinicalTermVo {

    private String verificationId;

    private String categoryId;

    private String severityId;

    private LocalDate date;

    public AllergyConditionVo(Integer id, Snomed snomed, String statusId, String verificationId,
                             String categoryId, LocalDate date) {
        super(id, snomed, statusId);
        this.verificationId = verificationId;
        this.severityId = null;
        this.categoryId = categoryId;
        this.date = date;
    }
}
