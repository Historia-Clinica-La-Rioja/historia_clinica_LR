package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.hce.entity;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.Snomed;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class HCEPersonalHistoryVo extends HCEHealthConditionVo {

    private String type;

    public HCEPersonalHistoryVo(Integer id, Snomed snomed, String statusId, boolean main, String verificationId,
                                String problemId, String severity, LocalDate startDate, LocalDate inactivationDate,
                                Integer patientId, String institutionName, Integer professionalUserId, LocalDateTime createdOn,
                                String note, String type) {
        super(id, snomed, statusId, main, verificationId, problemId, severity, startDate, inactivationDate, patientId, institutionName, professionalUserId, createdOn, note);
        this.type = type;
    }
}
