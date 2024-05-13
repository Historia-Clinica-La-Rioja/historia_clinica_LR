package ar.lamansys.sgh.clinichistory.domain.document;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class DocumentHeaderBo implements IDocumentHeaderBo {

    private Long id;
    private SourceTypeBo sourceType;
    private LocalDateTime createdOn;
    private Integer createdBy;
    private Integer institutionId;
    private Integer encounterId;
    private Integer clinicalSpecialtyId;
}
