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

@Entity
@Table(name = "institution")
@Getter
@Setter

public class Institution {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column(name = "name", nullable = false)
	private String name;
	
	@Column(name = "address_id", nullable = false)
	private int address_id;
	
	@Column(name = "website", nullable = false)
	private String website;
	
	@Column(name = "phone", nullable = false)
	private String phone;
	
	@Column(name = "email", nullable = false)
	private String email;
	
	@Column(name = "cuit", nullable = false)
	private String cuit;
	
	@Column(name = "sisa_code", nullable = false)
	private String sisa_code;

	@Override
	public String toString() {
		return "Institution [id=" + id + ", name=" + name + ", address_id=" + address_id + ", website=" + website
				+ ", phone=" + phone + ", email=" + email + ", cuit=" + cuit + ", sisa_code=" + sisa_code + "]";
	}
	
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
