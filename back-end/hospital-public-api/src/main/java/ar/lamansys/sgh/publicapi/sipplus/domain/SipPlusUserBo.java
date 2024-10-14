package ar.lamansys.sgh.publicapi.sipplus.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import net.minidev.json.JSONArray;

import java.util.List;

@Getter
@Setter
@Builder
@ToString
public class SipPlusUserBo {

	private String id;

	private String userName;

	private String fullName;

	private String countryId;

	private JSONArray roles;

	private List<SipPlusInstitutionBo> institutions;

	private List<SipPlusInstitutionBo> readableInstitutions;
}
