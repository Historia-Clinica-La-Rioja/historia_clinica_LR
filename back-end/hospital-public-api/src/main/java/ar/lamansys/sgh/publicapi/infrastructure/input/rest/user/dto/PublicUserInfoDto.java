package ar.lamansys.sgh.publicapi.infrastructure.input.rest.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PublicUserInfoDto {

	private Integer id;

	private String username;
}
