package net.pladema.internation.repository.core.domain;

import lombok.*;
import net.pladema.internation.repository.masterdata.entity.DocumentStatus;

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
