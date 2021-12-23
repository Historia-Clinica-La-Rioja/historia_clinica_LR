package ar.lamansys.sgh.shared.infrastructure.input.service;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDate;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CounterReferenceSummaryProcedureDto implements Serializable {

    private static final long serialVersionUID = 2806473937158573659L;

    private SharedSnomedDto snomed;

}
