package net.pladema.establishment.infrastructure.port;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.establishment.application.port.InstitutionalGroupStorage;

import net.pladema.establishment.repository.InstitutionalGroupRepository;

import net.pladema.establishment.service.domain.InstitutionalGroupBo;

import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class InstitutionalGroupStorageImpl implements InstitutionalGroupStorage {

	private final InstitutionalGroupRepository institutionalGroupRepository;

	@Override
	public List<InstitutionalGroupBo> getInstitutionalGroupsByUserId(Integer userId){
		log.debug("Input parameters -> userId {}", userId);
		List<InstitutionalGroupBo> result = institutionalGroupRepository.getInstitutionalGroupByUserId(userId).stream().map(InstitutionalGroupBo::new).collect(Collectors.toList());
		log.debug("Output -> result {}", result);
		return result;
	}


}
