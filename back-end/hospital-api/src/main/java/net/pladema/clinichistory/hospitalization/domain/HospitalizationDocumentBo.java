package net.pladema.clinichistory.hospitalization.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class HospitalizationDocumentBo {

    private String reason;
    private Integer createdBy;
    private Integer sourceId;
    private LocalDateTime createdOn;
}
