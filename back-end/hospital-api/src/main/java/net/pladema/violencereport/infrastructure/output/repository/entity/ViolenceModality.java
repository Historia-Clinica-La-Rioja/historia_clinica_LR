package net.pladema.violencereport.infrastructure.output.repository.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.pladema.violencereport.infrastructure.output.repository.embedded.ViolenceReportSnomedPK;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import java.io.Serializable;

@NoArgsConstructor
@Getter
@Setter
@ToString
@Table(name = "violence_modality")
@Entity
public class ViolenceModality implements Serializable {

	private static final long serialVersionUID = 7274264513191858736L;

	@EmbeddedId
	private ViolenceReportSnomedPK pk;

}
