package net.pladema.establishment.application.institutionalgroups;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import net.pladema.establishment.application.port.InstitutionalGroupStorage;

import net.pladema.establishment.service.domain.InstitutionalGroupBo;

import org.springframework.stereotype.Service;

import java.util.List;


@Slf4j
@AllArgsConstructor
@Service
public class GetInstitutionalGroupsByUserId {

	private final InstitutionalGroupStorage institutionalGroupStorage;

	public List<InstitutionalGroupBo> run (Integer userId){
		log.debug("Input parameters -> userId {}", userId);
		List<InstitutionalGroupBo> result = institutionalGroupStorage.getInstitutionalGroupsByUserId(userId);
		log.debug("Output -> result {}", result);
		return result;
	}

}
