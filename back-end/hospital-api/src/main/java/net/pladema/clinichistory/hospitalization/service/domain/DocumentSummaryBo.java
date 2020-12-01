package net.pladema.clinichistory.hospitalization.service.domain;

import lombok.*;
import net.pladema.clinichistory.documents.repository.ips.masterdata.entity.DocumentStatus;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public abstract class DocumentSummaryBo {

    private Long id;

    private String statusId;

    public Boolean isConfirmed(){
        return (statusId != null && statusId.equals(DocumentStatus.FINAL));
    }
}
