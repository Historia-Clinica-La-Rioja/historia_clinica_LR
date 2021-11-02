package ar.lamansys.odontology.infrastructure.controller.consultation.dto;

import ar.lamansys.sgx.shared.dates.controller.dto.DateDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class OdontologyConsultationIndicesDto implements Serializable {

    private Integer permanentC;

    private Integer permanentP;

    private Integer permanentO;

    private Integer temporaryC;

    private Integer temporaryE;

    private Integer temporaryO;

    private Integer cpoIndex;

    private Integer ceoIndex;

    private Integer permanentTeethPresent;

    private Integer temporaryTeethPresent;

    private DateDto date;

}
