package net.pladema.violencereport.infrastructure.output.repository.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.pladema.violencereport.infrastructure.output.repository.embedded.NationalStateDevicePK;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import java.io.Serializable;

@NoArgsConstructor
@Getter
@Setter
@ToString
@Table(name = "national_state_device")
@Entity
public class NationalStateDevice implements Serializable {

	private static final long serialVersionUID = -3414797512816008434L;

	@EmbeddedId
	private NationalStateDevicePK pk;

}
