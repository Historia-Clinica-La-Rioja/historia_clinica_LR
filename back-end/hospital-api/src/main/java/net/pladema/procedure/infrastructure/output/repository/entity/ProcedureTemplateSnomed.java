package net.pladema.procedure.infrastructure.output.repository.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import java.io.Serializable;

@Entity
@Table(name = "procedure_template_snomed")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class ProcedureTemplateSnomed implements Serializable {

	@EmbeddedId
	private ProcedureTemplateSnomedPK pk;

	public ProcedureTemplateSnomed(Integer procedureTemplateId, Integer snomedId) {
		this.pk = new ProcedureTemplateSnomedPK(procedureTemplateId,snomedId);
	}
}
