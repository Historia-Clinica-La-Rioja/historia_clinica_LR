package ar.lamansys.sgx.auth.apiKey.infrastructure.output.repository.userkey;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "user_key", uniqueConstraints = {
		@UniqueConstraint(columnNames = {"user_id", "name"})
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserKey {

	@Id
	@Column(name = "key", nullable = false, length = 255)
	private String key;

	@Column(name = "user_id", nullable = false)
	private Integer userId;

	@Column(name = "name", nullable = false, length = 255)
	private String name;
}
