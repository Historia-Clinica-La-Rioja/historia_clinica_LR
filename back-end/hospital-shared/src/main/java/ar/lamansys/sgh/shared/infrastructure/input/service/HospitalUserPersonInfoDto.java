package ar.lamansys.sgh.shared.infrastructure.input.service;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HospitalUserPersonInfoDto {

	private Integer id;

	private String username;

	private String email;

	private Integer personId;

	private String firstName;

	private String lastName;

	private String nameSelfDetermination;
}
