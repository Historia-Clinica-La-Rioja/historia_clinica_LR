package net.pladema.establishment.application.carelineinstitutionpractices;


import ar.lamansys.sgh.clinichistory.domain.ips.SnomedBo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import net.pladema.establishment.application.port.CareLineInstitutionPracticeStorage;

import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Service
public class GetCareLineInstitutionPractices {

	private final CareLineInstitutionPracticeStorage careLineInstitutionPracticeStorage;

	public List<SnomedBo> run(Integer careLineId) {
		log.debug("Input parameter -> careLineId {} ", careLineId);
        return careLineInstitutionPracticeStorage.fetchPracticesByCareLineId(careLineId);
	}

}
