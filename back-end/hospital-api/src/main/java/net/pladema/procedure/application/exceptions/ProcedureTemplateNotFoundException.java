package net.pladema.procedure.application.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@ToString
@AllArgsConstructor
@Getter
public class ProcedureTemplateNotFoundException extends Exception {
	private final Integer procedureTemplateId;
}
