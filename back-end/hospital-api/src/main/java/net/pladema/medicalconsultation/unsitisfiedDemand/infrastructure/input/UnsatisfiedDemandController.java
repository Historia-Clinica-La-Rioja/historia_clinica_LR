package net.pladema.medicalconsultation.unsitisfiedDemand.infrastructure.input;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import net.pladema.medicalconsultation.unsitisfiedDemand.application.SaveUnsatisfiedAppointmentDemand;
import net.pladema.medicalconsultation.unsitisfiedDemand.domain.UnsatisfiedAppointmentDemandBo;
import net.pladema.medicalconsultation.unsitisfiedDemand.infrastructure.input.dto.UnsatisfiedAppointmentDemandDto;

import net.pladema.medicalconsultation.unsitisfiedDemand.infrastructure.input.mapper.UnsatisfiedAppointmentDemandMapper;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@AllArgsConstructor
@Tag(name = "Register unsatisfied demand", description = "This controller is used to register unsatisfied appointment demand")
@RestController
@RequestMapping(value = "/institution/{institutionId}/unsatisfied-demand/register")
public class UnsatisfiedDemandController {

	private UnsatisfiedAppointmentDemandMapper unsatisfiedAppointmentDemandMapper;

	private SaveUnsatisfiedAppointmentDemand saveUnsatisfiedAppointmentDemand;

	@PostMapping
	@PreAuthorize("hasPermission(#institutionId, 'ADMINISTRADOR_AGENDA, ADMINISTRATIVO')")
	public Integer run(@PathVariable(name = "institutionId") Integer institutionId,
					   @RequestBody UnsatisfiedAppointmentDemandDto unsatisfiedDemand) {
		log.debug("Input parameters -> institutionId {}, unsatisfiedDemand {}", institutionId, unsatisfiedDemand);
		UnsatisfiedAppointmentDemandBo unsatisfiedAppointmentDemandBo = parseBo(institutionId, unsatisfiedDemand);
		Integer result = saveUnsatisfiedAppointmentDemand.run(unsatisfiedAppointmentDemandBo);
		log.debug("Output -> {}", result);
		return result;
	}
	
	private UnsatisfiedAppointmentDemandBo parseBo(Integer institutionId, UnsatisfiedAppointmentDemandDto unsatisfiedDemand) {
		UnsatisfiedAppointmentDemandBo unsatisfiedAppointmentDemandBo = unsatisfiedAppointmentDemandMapper.fromUnsatisfiedDemandDto(unsatisfiedDemand);
		unsatisfiedAppointmentDemandBo.setInstitutionId(institutionId);
		return unsatisfiedAppointmentDemandBo;
	}

}
