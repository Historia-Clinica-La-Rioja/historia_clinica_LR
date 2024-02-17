package net.pladema.audit.controller;


import lombok.RequiredArgsConstructor;
import net.pladema.audit.repository.ViewClinicHistoryAuditRepository;
import net.pladema.audit.repository.entity.ViewClinicHistoryAudit;
import net.pladema.sgx.backoffice.repository.BackofficeStore;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BackofficeViewClinicHistoryAuditStore implements BackofficeStore<ViewClinicHistoryAudit, Integer> {


	private final ViewClinicHistoryAuditRepository viewClinicHistoryAuditRepository;

	@Override
	public Page<ViewClinicHistoryAudit> findAll(ViewClinicHistoryAudit entity, Pageable pageable) {
		if ((entity.getIdentificationNumber() != null))
			entity.setIdentificationNumber(entity.getIdentificationNumber().replace(".", ""));

		return viewClinicHistoryAuditRepository.findAll(buildExample(entity), pageable);
	}

	@Override
	public List<ViewClinicHistoryAudit> findAll() {
		return viewClinicHistoryAuditRepository.findAll();
	}

	@Override
	public List<ViewClinicHistoryAudit> findAllById(List<Integer> ids) {
		return viewClinicHistoryAuditRepository.findAllById(ids);
	}

	@Override
	public Optional<ViewClinicHistoryAudit> findById(Integer id) {
		return viewClinicHistoryAuditRepository.findById(id);
	}

	@Override
	public ViewClinicHistoryAudit save(ViewClinicHistoryAudit entity) {
		return null;
	}

	@Override
	public void deleteById(Integer id) {

	}


	@Override
	public Example<ViewClinicHistoryAudit> buildExample(ViewClinicHistoryAudit entity) {
		ExampleMatcher customExampleMatcher = ExampleMatcher.matching().withMatcher("identificationNumber",
				ExampleMatcher.GenericPropertyMatcher::startsWith)
				.withMatcher("identificationNumber", ExampleMatcher.GenericPropertyMatcher::startsWith)
				.withMatcher("firstName", x -> x.ignoreCase().contains())
				.withMatcher("lastName", x -> x.ignoreCase().contains())
				.withMatcher("description", x -> x.ignoreCase().contains())
				.withMatcher("username", x -> x.ignoreCase().contains())
				.withMatcher("institutionName", x -> x.ignoreCase().contains())
				.withMatcher("reasonId", x -> x.ignoreCase().contains());


		return Example.of(entity, customExampleMatcher);
	}

}
