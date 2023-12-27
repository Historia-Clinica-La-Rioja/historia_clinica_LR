package net.pladema.violencereport.infrastructure.output.repository.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.pladema.violencereport.infrastructure.output.repository.embedded.SexualViolenceActionPK;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import java.io.Serializable;

@NoArgsConstructor
@Getter
@Setter
@ToString
@Table(name = "sexual_violence_action")
@Entity
public class SexualViolenceAction implements Serializable {

	private static final long serialVersionUID = 7835431159492334143L;

	@EmbeddedId
	private SexualViolenceActionPK pk;

}
