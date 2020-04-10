package net.pladema.establishment.repository.entity;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "institution")
@Getter
@Setter
@ToString

public class Institution {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
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

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Institution other = (Institution) obj;
		return Objects.equals(id, other.id);
	}	
}
