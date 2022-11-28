package net.pladema.establishment.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import net.pladema.establishment.repository.CareLineInstitutionSpecialtyRepository;
import net.pladema.establishment.repository.VClinicalSpecialtyInstitutionRepository;
import net.pladema.establishment.repository.entity.VClinicalSpecialtyInstitution;
import net.pladema.sgx.backoffice.repository.BackofficeRepository;
import net.pladema.sgx.backoffice.rest.AbstractBackofficeController;
import net.pladema.sgx.backoffice.rest.SingleAttributeBackofficeQueryAdapter;

@RestController
@RequestMapping("backoffice/carelinespecialtyinstitution")
public class CareLineClinicalSpecialtyInstitutionController extends AbstractBackofficeController<VClinicalSpecialtyInstitution, Integer> {

	private final CareLineInstitutionSpecialtyRepository careLineInstitutionSpecialtyRepository;

	public CareLineClinicalSpecialtyInstitutionController(VClinicalSpecialtyInstitutionRepository repository,
														  CareLineInstitutionSpecialtyRepository careLineInstitutionSpecialtyRepository) {
		super(new BackofficeRepository<>(
				repository,
				new SingleAttributeBackofficeQueryAdapter<>("description")
		));
		this.careLineInstitutionSpecialtyRepository = careLineInstitutionSpecialtyRepository;
	}

	@Override
	public Page<VClinicalSpecialtyInstitution> getList(Pageable pageable, VClinicalSpecialtyInstitution entity) {
		var page = super.getList(pageable, entity);
		var content = page.getContent()
				.stream()
				.distinct()
				.filter(item -> this.careLineInstitutionSpecialtyRepository.findByCareLineIdAndInstitutionIdAndClinicalSpecialtyId(item.getCareLineId(),
						item.getInstitutionId(),
						item.getId()).isEmpty())
				.collect(Collectors.toList());
		return new PageImpl<>(content, pageable, content.size());
	}

	@Override
	public Iterable<VClinicalSpecialtyInstitution> getMany(@RequestParam List<Integer> ids) {
		return new ArrayList<>();
	}
}
