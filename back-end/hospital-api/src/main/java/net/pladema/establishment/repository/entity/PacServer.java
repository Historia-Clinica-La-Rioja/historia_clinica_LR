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
import lombok.ToString;

@Entity
@Table(name = "pac_server")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class PacServer {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "name", nullable = false, length = 20)
	private String name;

	@Column(name = "aetitle", nullable = false, length = 15)
	private String aetitle;

	@Column(name = "domain", nullable = false, length = 50)
	private String domain;

	@Column(name = "pac_server_type_id", nullable = false)
	private Short pacServerType;

	@Column(name = "pac_server_protocol_id", nullable = false)
	private Short pacServerProtocol;

	@Column(name = "username", nullable = false, length = 50)
	private String username;

	@Column(name = "password", nullable = false)
	private String password;

	@Column(name = "url_stow", nullable = false)
	private String urlStow;

	@Column(name = "url_auth", nullable = false)
	private String urlAuth;

	@Column(name = "institution_id", nullable = true)
	private Integer institutionId;

}
