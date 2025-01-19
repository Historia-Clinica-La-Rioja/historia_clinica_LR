package net.pladema.loinc.infrastructure.output.entity;

import ar.lamansys.sgx.shared.auditable.entity.SGXAuditableEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "loinc_code")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class LoincCode extends SGXAuditableEntity<Integer> implements Serializable {

	@Id
	@Column(name = "id")
	private Integer id;

	@Column(name = "status_id", nullable = false)
	private Short statusId;

	@Column(name = "system_id", nullable = false)
	private Short systemId;

	@Column(name = "description", nullable = false)
	private String description;

	@Column(name = "code", nullable = false)
	private String code;

	@Column(name = "display_name", nullable = true)
	private String displayName;

	@Column(name = "custom_display_name", nullable = true)
	private String customDisplayName;

    public boolean updateAllowed(LoincCode original) {
    	return Objects.equals(this.id, original.id) &&
				Objects.equals(this.statusId, original.statusId) &&
				Objects.equals(this.systemId, original.systemId) &&
				Objects.equals(this.description, original.description) &&
				Objects.equals(this.code, original.code) &&
				Objects.equals(this.displayName, original.displayName);
    }
}

