package net.pladema.establishment.controller;

import lombok.AllArgsConstructor;
import net.pladema.establishment.repository.CareLineRoleRepository;
import net.pladema.establishment.repository.entity.CareLineRole;
import net.pladema.sgx.backoffice.repository.BackofficeStore;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class BackofficeCareLineRoleStore implements BackofficeStore<CareLineRole, Integer> {

	private CareLineRoleRepository careLineRoleRepository;

	@Override
	public Page<CareLineRole> findAll(CareLineRole example, Pageable pageable) {
		return careLineRoleRepository.findAll(buildExample(example), pageable);
	}

	@Override
	public List<CareLineRole> findAll() {
		return careLineRoleRepository.findAll();
	}

	@Override
	public List<CareLineRole> findAllById(List<Integer> ids) {
		return careLineRoleRepository.findAllById(ids);
	}

	@Override
	public Optional<CareLineRole> findById(Integer id) {
		return careLineRoleRepository.findById(id);
	}

	@Override
	public CareLineRole save(CareLineRole entity) {
		return careLineRoleRepository.save(entity);
	}

	@Override
	public void deleteById(Integer id) {
		careLineRoleRepository.deleteById(id);
	}

	@Override
	public Example<CareLineRole> buildExample(CareLineRole entity) {
		CareLineRole careLineRole = new CareLineRole();
		careLineRole.setDeleted(false);
		careLineRole.setCareLineId(entity.getCareLineId());
		return Example.of(careLineRole);
	}

}
