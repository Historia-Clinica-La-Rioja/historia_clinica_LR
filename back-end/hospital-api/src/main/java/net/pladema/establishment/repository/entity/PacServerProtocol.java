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
@Table(name = "pac_server_protocol")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PacServerProtocol {

	public static final Short SFTP = (short) 1;
	public static final Short DICOM_WEB = (short) 2;

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Short id;

	@Column(name = "description", length = 50)
	private String description;
}
