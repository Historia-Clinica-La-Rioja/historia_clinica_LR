package net.pladema.establishment.controller;

import ar.lamansys.sgh.clinichistory.domain.ips.services.SnomedService;
import ar.lamansys.sgx.shared.featureflags.AppFeature;
import ar.lamansys.sgx.shared.featureflags.application.FeatureFlagsService;


import net.pladema.establishment.controller.dto.BackofficeSnowstormDto;

import net.pladema.establishment.controller.dto.SnomedProblemDto;
import net.pladema.sgx.backoffice.rest.AbstractBackofficeController;
import net.pladema.snowstorm.services.domain.SnomedSearchItemBo;
import net.pladema.snowstorm.services.domain.semantics.SnomedECL;

import net.pladema.snowstorm.services.searchCachedConcepts.SearchCachedConcepts;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("backoffice/snomedproblems")
public class BackofficeSnomedProblemsController extends AbstractBackofficeController<SnomedProblemDto, Long> {

	public BackofficeSnomedProblemsController(BackofficeSnomedProblemsStore store)
	{
		super(store);
	}

}