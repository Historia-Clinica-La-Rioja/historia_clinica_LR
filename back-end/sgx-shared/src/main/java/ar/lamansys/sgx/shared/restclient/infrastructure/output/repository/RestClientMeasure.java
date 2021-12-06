package ar.lamansys.sgx.shared.restclient.infrastructure.output.repository;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "rest_client_measure")
@Getter
@Setter
@NoArgsConstructor
public class RestClientMeasure {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3053291021636483828L;

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "uri", nullable = false, length = 1024)
	private String uri;

	@Column(name = "host", nullable = false)
	private String host;

	@Column(name = "path", nullable = false)
	private String path;

	@Column(name = "method", nullable = false, length = 8)
	private String method;

	@Column(name = "response_code", nullable = false)
	private Short responseCode;

	@Column(name = "response_time_in_millis", nullable = false)
	private Long responseTimeInMillis;

	@Column(name = "request_date")
	private LocalDateTime requestDate;

	public RestClientMeasure(String uri, String host, String path, String method, Short responseCode, Long responseTimeInMillis, LocalDateTime requestDate) {
		this.uri = uri;
		this.host = host;
		this.path = path;
		this.method = method;
		this.responseCode = responseCode;
		this.responseTimeInMillis = responseTimeInMillis;
		this.requestDate = requestDate;
	}
}
