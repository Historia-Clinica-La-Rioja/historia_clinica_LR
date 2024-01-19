package net.pladema.establishment.application.practices;

import ar.lamansys.sgh.shared.infrastructure.input.service.SharedSnomedDto;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import net.pladema.snowstorm.infrastructure.input.service.SnomedRelatedGroupExternalService;

import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class GetPracticesByDepartmentId {

	private final SnomedRelatedGroupExternalService snomedRelatedGroupExternalService;

	public List<SharedSnomedDto> run(Short departmentId) {
		log.debug("Input parameter -> departmentId {} ", departmentId);
		return snomedRelatedGroupExternalService.getPracticesByDepartmentId(departmentId);
	}
}
