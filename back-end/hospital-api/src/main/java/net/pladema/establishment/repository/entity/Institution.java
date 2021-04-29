package net.pladema.establishment.repository.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import net.pladema.sgx.dates.configuration.JacksonDateFormatConfig;

@Entity
@Table(name = "institution")
@Getter
@Setter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)

public class Institution {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@EqualsAndHashCode.Include
	private Integer id;
	
	@Column(name = "name", nullable = false, length = 255)
	private String name;
	
	@Column(name = "address_id", nullable = false)
	private Integer addressId;
	
	@Column(name = "website", nullable = true, length = 255)
	private String website;
	
	@Column(name = "phone_number", nullable = false, length = 15)
	private String phone;
	
	@Column(name = "email", nullable = false, length = 100)
	private String email;
	
	@Column(name = "cuit", nullable = false, length = 20)
	private String cuit;
	
	@Column(name = "sisa_code", nullable = false, length = 15)
	private String sisaCode;

	@Column(name = "timezone", nullable = false, length = 60)
	private String timezone = JacksonDateFormatConfig.ZONE_ID;

}
