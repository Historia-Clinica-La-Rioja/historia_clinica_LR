package net.pladema.person.repository.domain;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ProcedureReduced {

    @EqualsAndHashCode.Include
    private String procedure;

    @EqualsAndHashCode.Include
    private LocalDate performedDate;

    public ProcedureReduced(String procedure, LocalDate performedDate){
        this.procedure = procedure;
        this.performedDate = performedDate;
    }

}
