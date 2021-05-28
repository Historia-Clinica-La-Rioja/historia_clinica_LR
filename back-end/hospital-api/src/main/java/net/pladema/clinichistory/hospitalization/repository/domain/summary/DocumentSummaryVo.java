package net.pladema.clinichistory.hospitalization.repository.domain.summary;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentStatus;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public abstract class DocumentSummaryVo {

    private Long id;

    private String statusId;

    public Boolean isConfirmed(){
        return (statusId != null && statusId.equals(DocumentStatus.FINAL));
    }
}
