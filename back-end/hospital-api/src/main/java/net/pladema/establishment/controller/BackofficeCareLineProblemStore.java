package net.pladema.establishment.controller;

import net.pladema.establishment.repository.CareLineProblemRepository;
import net.pladema.establishment.repository.entity.CareLineProblem;
import net.pladema.sgx.backoffice.repository.BackofficeStore;

import net.pladema.sgx.exceptions.BackofficeValidationException;
import net.pladema.snowstorm.repository.SnomedRelatedGroupRepository;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class BackofficeCareLineProblemStore implements BackofficeStore<CareLineProblem, Integer> {

	private final CareLineProblemRepository careLineProblemRepository;

	private final SnomedRelatedGroupRepository snomedRelatedGroupRepository;

	public BackofficeCareLineProblemStore(CareLineProblemRepository careLineProblemRepository,
										  SnomedRelatedGroupRepository snomedRelatedGroupRepository){
		this.careLineProblemRepository=careLineProblemRepository;
		this.snomedRelatedGroupRepository=snomedRelatedGroupRepository;
	}


	@Override
	public Page<CareLineProblem> findAll(CareLineProblem entity, Pageable pageable) {
		List<CareLineProblem> result = careLineProblemRepository.findByCareLineId(entity.getCareLineId());
		return new PageImpl<>(result, pageable, result.size());
	}

	@Override
	public List<CareLineProblem> findAll() {
		return careLineProblemRepository.findAll();
	}

	@Override
	public List<CareLineProblem> findAllById(List<Integer> ids) {
		return careLineProblemRepository.findAllById(ids);
	}

	@Override
	public Optional<CareLineProblem> findById(Integer id) {
		return careLineProblemRepository.findById(id);
	}

	@Override
	@PreAuthorize("hasAnyAuthority('ROOT', 'ADMINISTRADOR')")
	@Transactional
	public CareLineProblem save(CareLineProblem entity) {
		Optional<Integer> snomedId = snomedRelatedGroupRepository.getSnomedIdById(entity.getSnomedId());
		if(snomedId.isPresent()){
			entity.setSnomedId(snomedId.get());
			Optional<CareLineProblem> clp = careLineProblemRepository.findByCareLineIdAndSnomedId(entity.getCareLineId(), entity.getSnomedId());
			if(clp.isPresent()) {
				if (clp.get().isDeleted()) {
					clp.get().setDeleted(false);
					return careLineProblemRepository.save(clp.get());
				} else {
					throw new BackofficeValidationException("care-line.problem.exists");
				}
			}
			return careLineProblemRepository.save(entity);
		}
		return entity;
	}

	@Override
	@PreAuthorize("hasAnyAuthority('ROOT', 'ADMINISTRADOR')")
	public void deleteById(Integer id) {
		careLineProblemRepository.deleteById(id);
	}

	@Override
	public Example<CareLineProblem> buildExample(CareLineProblem entity) {
		return Example.of(entity);
	}

}
