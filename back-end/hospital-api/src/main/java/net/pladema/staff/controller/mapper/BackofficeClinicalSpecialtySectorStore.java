package net.pladema.staff.controller.mapper;

import net.pladema.establishment.repository.ClinicalSpecialtySectorRepository;
import net.pladema.establishment.repository.VClinicalSpecialtySectorRepository;
import net.pladema.establishment.repository.entity.ClinicalSpecialtySector;
import net.pladema.establishment.repository.entity.VClinicalSpecialtySector;
import net.pladema.sgx.backoffice.repository.BackofficeStore;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BackofficeClinicalSpecialtySectorStore implements BackofficeStore<ClinicalSpecialtySector, Integer> {

	private final ClinicalSpecialtySectorRepository clinicalSpecialtySectorRepository;

	private final VClinicalSpecialtySectorRepository vClinicalSpecialtySectorRepository;

	public BackofficeClinicalSpecialtySectorStore(ClinicalSpecialtySectorRepository clinicalSpecialtySectorRepository,
												  VClinicalSpecialtySectorRepository vClinicalSpecialtySectorRepository) {
		this.clinicalSpecialtySectorRepository = clinicalSpecialtySectorRepository;
		this.vClinicalSpecialtySectorRepository = vClinicalSpecialtySectorRepository;
	}

	@Override
	public Page<ClinicalSpecialtySector> findAll(ClinicalSpecialtySector example, Pageable pageable) {
		Example<VClinicalSpecialtySector> entity = buildExample(new VClinicalSpecialtySector(example));
		return vClinicalSpecialtySectorRepository.findAll(entity, pageable).map(VClinicalSpecialtySector::parseToClinicalSpecialtySector);
	}

	@Override
	public List<ClinicalSpecialtySector> findAll() {
		return vClinicalSpecialtySectorRepository.findAll().stream().map(VClinicalSpecialtySector::parseToClinicalSpecialtySector).collect(Collectors.toList());
	}

	@Override
	public List<ClinicalSpecialtySector> findAllById(List<Integer> ids) {
		return clinicalSpecialtySectorRepository.findAllById(ids);
	}

	@Override
	public Optional<ClinicalSpecialtySector> findById(Integer id) {
		return clinicalSpecialtySectorRepository.findById(id);
	}

	@Override
	public ClinicalSpecialtySector save(ClinicalSpecialtySector entity) {
		return clinicalSpecialtySectorRepository.save(entity);
	}

	@Override
	public void deleteById(Integer id) {
		clinicalSpecialtySectorRepository.deleteById(id);
	}

	@Override
	public Example<ClinicalSpecialtySector> buildExample(ClinicalSpecialtySector entity) {
		return null;
	}

	public Example<VClinicalSpecialtySector> buildExample(VClinicalSpecialtySector entity) {
		ExampleMatcher matcher = ExampleMatcher.matching().withMatcher("description", x -> x.ignoreCase().contains());
		return Example.of(entity, matcher);
	}
}
