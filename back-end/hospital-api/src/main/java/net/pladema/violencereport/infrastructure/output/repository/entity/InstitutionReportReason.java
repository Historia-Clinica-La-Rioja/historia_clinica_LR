package net.pladema.violencereport.infrastructure.output.repository.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.pladema.violencereport.infrastructure.output.repository.embedded.InstitutionReportReasonPK;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import java.io.Serializable;

@NoArgsConstructor
@Getter
@Setter
@ToString
@Table(name = "institution_report_reason")
@Entity
public class InstitutionReportReason implements Serializable {

	private static final long serialVersionUID = -947200382222128399L;

	@EmbeddedId
	private InstitutionReportReasonPK pk;

}
