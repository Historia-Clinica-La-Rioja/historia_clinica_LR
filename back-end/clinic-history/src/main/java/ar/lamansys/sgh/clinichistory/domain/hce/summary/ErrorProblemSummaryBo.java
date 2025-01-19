package ar.lamansys.sgh.clinichistory.domain.hce.summary;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.masterdata.entity.EProblemErrorReason;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class ErrorProblemSummaryBo {
    private LocalDateTime timePerformed;
    private String reason;
    private String observations;

    public ErrorProblemSummaryBo(LocalDateTime timePerformed, Short reasonId, String observations) {
        this.timePerformed = timePerformed;
        this.reason = EProblemErrorReason.map(reasonId).getDescription();
        this.observations = observations;
    }
}
