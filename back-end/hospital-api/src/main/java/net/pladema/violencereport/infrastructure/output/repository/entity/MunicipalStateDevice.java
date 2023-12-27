package net.pladema.violencereport.infrastructure.output.repository.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.pladema.violencereport.infrastructure.output.repository.embedded.MunicipalStateDevicePK;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import java.io.Serializable;

@NoArgsConstructor
@Getter
@Setter
@ToString
@Table(name = "municipal_state_device")
@Entity
public class MunicipalStateDevice implements Serializable {

	private static final long serialVersionUID = 4591918722882688821L;

	@EmbeddedId
	private MunicipalStateDevicePK pk;

}
