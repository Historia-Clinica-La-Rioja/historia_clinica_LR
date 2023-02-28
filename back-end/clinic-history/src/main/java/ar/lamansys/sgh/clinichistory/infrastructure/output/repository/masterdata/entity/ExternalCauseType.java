package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.masterdata.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name = "external_cause_type")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class ExternalCauseType {

	public static final String ACCIDENT = "55566008";
	public static final String SELF_INFLICTED_INJURY = "276853009";
	public static final String AGRESSION = "61372001";
	public static final String IGNORED = "00000001";

		@Id
		@Column(name = "id", length = 20)
		private String id;

		@Column(name = "description", nullable = false, length = 100)
		private String description;

}


