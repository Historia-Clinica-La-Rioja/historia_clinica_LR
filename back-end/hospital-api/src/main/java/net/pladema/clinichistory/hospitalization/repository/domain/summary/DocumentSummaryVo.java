package net.pladema.clinichistory.hospitalization.repository.domain.summary;

import lombok.*;
import net.pladema.clinichistory.documents.repository.ips.masterdata.entity.DocumentStatus;

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
