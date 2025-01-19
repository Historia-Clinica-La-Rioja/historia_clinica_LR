package ar.lamansys.sgh.shared.infrastructure.input.service.institutionalgroup;

import java.util.List;

public interface SharedInstitutionalGroupPort {

	List<Integer> getInstitutionIdsByUserId(Integer userId);

}
