package net.pladema.violencereport.infrastructure.output.repository.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.pladema.violencereport.infrastructure.output.repository.embedded.ProvincialStateDevicePK;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import java.io.Serializable;

@NoArgsConstructor
@Getter
@Setter
@ToString
@Table(name = "provincial_state_device")
@Entity
public class ProvincialStateDevice implements Serializable {

	private static final long serialVersionUID = 6473104103415701629L;

	@EmbeddedId
	private ProvincialStateDevicePK pk;

}
