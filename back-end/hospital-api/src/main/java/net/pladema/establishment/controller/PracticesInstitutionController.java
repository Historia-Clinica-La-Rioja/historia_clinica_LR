package net.pladema.establishment.controller;

import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import net.pladema.establishment.repository.CareLineInstitutionPracticeRepository;
import net.pladema.establishment.repository.VPracticeInstitutionRepository;
import net.pladema.establishment.repository.entity.VPracticeInstitution;
import net.pladema.sgx.backoffice.repository.BackofficeRepository;
import net.pladema.sgx.backoffice.rest.AbstractBackofficeController;
import net.pladema.sgx.backoffice.rest.SingleAttributeBackofficeQueryAdapter;

@RestController
@RequestMapping("backoffice/practicesinstitution")
public class PracticesInstitutionController extends AbstractBackofficeController<VPracticeInstitution, Integer> {

	private final CareLineInstitutionPracticeRepository careLineInstitutionPracticeRepository;

	public PracticesInstitutionController(VPracticeInstitutionRepository repository, CareLineInstitutionPracticeRepository careLineInstitutionPracticeRepository) {
		super(new BackofficeRepository<>(
				repository,
				new SingleAttributeBackofficeQueryAdapter<>("description")
		));
		this.careLineInstitutionPracticeRepository = careLineInstitutionPracticeRepository;
	}

	public @ResponseBody
	Page<VPracticeInstitution> getList(Pageable pageable, VPracticeInstitution entity) {
		var page = super.getList(pageable, new VPracticeInstitution(null, entity.getInstitutionId(), null));
		Integer careLineInstitutionId = entity.getId();
		var pageContent = page.getContent()
				.stream()
				.filter(item -> careLineInstitutionPracticeRepository.findByCareLineInstitutionIdAndSnomedRelatedGroupId(careLineInstitutionId, item.getId())
						.isEmpty())
				.collect(Collectors.toList());
		return new PageImpl<>(pageContent, pageable, pageContent.size());
	}
}
