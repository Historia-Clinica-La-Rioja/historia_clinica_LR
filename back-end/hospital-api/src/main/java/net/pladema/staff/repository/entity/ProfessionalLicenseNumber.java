package net.pladema.staff.repository.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.pladema.staff.repository.entity.converters.LicenseNumberTypeConverter;
import net.pladema.staff.service.domain.ELicenseNumberTypeBo;

@Entity
@Table(name = "professional_license_numbers")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ProfessionalLicenseNumber implements Serializable {

	private static final long serialVersionUID = 7788785116152807155L;

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@ToString.Include
	private Integer id;

	@ToString.Include
	@Column(name = "license_number", nullable = false, length = 100)
	private String licenseNumber;

	@Convert(converter = LicenseNumberTypeConverter.class)
	@Column(name = "type_license_number", nullable = false)
	private ELicenseNumberTypeBo type;

	@ToString.Include
	@Column(name = "professional_profession_id")
	private Integer professionalProfessionId;

	@ToString.Include
	@Column(name = "healthcare_professional_specialty_id")
	private Integer healthcareProfessionalSpecialtyId;

	public ProfessionalLicenseNumber(Integer id, String licenseNumber, Short typeId, Integer professionalProfessionId,
									 Integer healthcareProfessionalSpecialtyId) {
		this.id = id;
		this.licenseNumber = licenseNumber;
		this.type = typeId != null ? ELicenseNumberTypeBo.map(typeId) : null;
		this.professionalProfessionId = professionalProfessionId;
		this.healthcareProfessionalSpecialtyId = healthcareProfessionalSpecialtyId;
	}
}
