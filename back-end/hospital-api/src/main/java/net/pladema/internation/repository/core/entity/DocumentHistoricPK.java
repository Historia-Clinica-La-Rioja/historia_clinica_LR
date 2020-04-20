package net.pladema.internation.repository.core.entity;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;


@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class DocumentHistoricPK implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = -3053291021636483828L;

	@Column(name = "id", nullable = false)
	private Long id;

	@Column(name = "created_on", nullable = false)
	private LocalDateTime createdOn;

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		DocumentHistoricPK that = (DocumentHistoricPK) o;
		return id.equals(that.id) &&
				createdOn.equals(that.createdOn);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, createdOn);
	}
}
