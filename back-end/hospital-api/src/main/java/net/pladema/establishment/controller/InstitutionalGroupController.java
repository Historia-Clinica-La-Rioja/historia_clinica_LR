package net.pladema.establishment.controller;

import ar.lamansys.sgx.shared.security.UserInfo;
import io.swagger.v3.oas.annotations.tags.Tag;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.establishment.application.institutionalgroups.GetInstitutionalGroupsByUserId;
import net.pladema.establishment.controller.dto.InstitutionalGroupDto;

import net.pladema.establishment.controller.mapper.InstitutionalGroupMapper;

import net.pladema.establishment.service.domain.InstitutionalGroupBo;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@Tag(name = "Institutional Groups", description = "Institutional Groups")
@RequestMapping("institutional-group")
@RequiredArgsConstructor
public class InstitutionalGroupController {

	private final GetInstitutionalGroupsByUserId getInstitutionalGroupsByUserId;

	private final InstitutionalGroupMapper institutionalGroupMapper;

	@GetMapping("/current-user")
	@PreAuthorize("hasAnyAuthority('GESTOR_DE_ACCESO_DE_DOMINIO', 'GESTOR_DE_ACCESO_REGIONAL', 'GESTOR_DE_ACCESO_LOCAL')")
	public List<InstitutionalGroupDto> getCurrentUserGroups(){
		log.debug("Get current user institutional groups");
		Integer currentUserId = UserInfo.getCurrentAuditor();
		List<InstitutionalGroupBo> institutionalGroupBos = getInstitutionalGroupsByUserId.run(currentUserId);
		List<InstitutionalGroupDto> result = institutionalGroupMapper.toListInstitutionalGroupDto(institutionalGroupBos);
		log.debug("Output -> {}", result);
		return result;
	}

}
