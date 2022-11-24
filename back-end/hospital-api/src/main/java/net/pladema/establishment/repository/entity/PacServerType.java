package net.pladema.establishment.repository.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "pac_server_type")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PacServerType {

	public static final Short CENTRAL_SERVER = (short) 1;
	public static final Short DIAGNOSTIC_CENTER = (short) 2;

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Short id;

	@Column(name = "description", length = 50)
	private String description;
}
