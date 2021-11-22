package net.pladema.nomivac.infrastructure.output.immunization.repository;

import ar.lamansys.sgx.shared.scheduling.infrastructure.output.repository.synchronization.AbstractSyncEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "nomivac_immunization_sync")
@NoArgsConstructor
@Getter
@Setter
public class NomivacImmunizationSync extends AbstractSyncEntity<Integer> {

	@Id
	@Column(name = "immunization_id")
	private Integer id;

	public NomivacImmunizationSync(Integer id) {
		this.id = id;
	}

	public NomivacImmunizationSync(Integer id, LocalDateTime synchronizedDate, Integer priority, String externalId, Integer statusCode) {
		super(synchronizedDate, priority, externalId, statusCode);
		this.id = id;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder("NomivacImmunizationSync{");
		sb.append("id=").append(id);
		sb.append(super.toString());
		sb.append('}');
		return sb.toString();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		NomivacImmunizationSync that = (NomivacImmunizationSync) o;
		return Objects.equals(id, that.id);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}
}
