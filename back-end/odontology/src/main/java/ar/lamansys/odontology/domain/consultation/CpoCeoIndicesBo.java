package ar.lamansys.odontology.domain.consultation;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CpoCeoIndicesBo {

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

    private LocalDate consultationDate;

}
