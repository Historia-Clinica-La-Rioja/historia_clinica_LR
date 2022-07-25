package ar.lamansys.sgh.publicapi.domain.user;

import ar.lamansys.sgh.publicapi.domain.authorities.PublicAuthorityBo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PublicUserInfoBo {

	private Integer id;

	private String username;
}
