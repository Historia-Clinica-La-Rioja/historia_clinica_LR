package net.pladema.person.repository.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class ProcedureReduced {

    private String procedure;

    private LocalDate performedDate;

    public ProcedureReduced(String procedure, LocalDate performedDate){
        this.procedure = procedure;
        this.performedDate = performedDate;
    }
}
