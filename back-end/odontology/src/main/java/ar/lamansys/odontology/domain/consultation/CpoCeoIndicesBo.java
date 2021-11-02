package ar.lamansys.odontology.domain.consultation;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CpoCeoIndicesBo {

    private Integer permanentC = 0;

    private Integer permanentP = 0;

    private Integer permanentO = 0;

    private Integer temporaryC = 0;

    private Integer temporaryE = 0;

    private Integer temporaryO = 0;

    private Integer cpoIndex;

    private Integer ceoIndex;

    private Integer permanentTeethPresent;

    private Integer temporaryTeethPresent;

    private LocalDateTime consultationDate;

}
