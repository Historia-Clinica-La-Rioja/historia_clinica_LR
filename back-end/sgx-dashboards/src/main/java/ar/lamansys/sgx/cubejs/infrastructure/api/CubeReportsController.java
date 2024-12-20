package ar.lamansys.sgx.cubejs.infrastructure.api;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.type.TypeReference;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.hsi.extensions.infrastructure.controller.dto.UIComponentDto;
import net.pladema.hsi.extensions.utils.JsonResourceUtils;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/reports")
@Tag(name = "Dashboards", description = "Dashboards")
public class CubeReportsController {

	@GetMapping("/institution/{institutionId}/diabetes")
	@PreAuthorize("hasPermission(#institutionId, 'ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD, ESPECIALISTA_EN_ODONTOLOGIA, ENFERMERO, PERSONAL_DE_ESTADISTICA, ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE')")
	public ResponseEntity<UIComponentDto> getDiabetesReport(@PathVariable(name = "institutionId") Integer institutionId){
		log.debug("Input parameter -> institutionId {}", institutionId);
		UIComponentDto result = JsonResourceUtils.readJson("extension/reports/diabetesReport.json",
				new TypeReference<>() {},
				null
		);
		return ResponseEntity.ok(result);
	}

	@GetMapping("/institution/{institutionId}/hypertension")
	@PreAuthorize("hasPermission(#institutionId, 'ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD, ESPECIALISTA_EN_ODONTOLOGIA, ENFERMERO, PERSONAL_DE_ESTADISTICA, ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE')")
	public ResponseEntity<UIComponentDto> getHypertensionReport(@PathVariable(name = "institutionId") Integer institutionId){
		log.debug("Input parameter -> institutionId {}", institutionId);
		UIComponentDto result = JsonResourceUtils.readJson("extension/reports/hypertensionReport.json",
				new TypeReference<>() {},
				null
		);
		return ResponseEntity.ok(result);
	}

	@GetMapping("/institution/{institutionId}/epidemiological_week")
	@PreAuthorize("hasPermission(#institutionId, 'ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD, ESPECIALISTA_EN_ODONTOLOGIA, ENFERMERO, PERSONAL_DE_ESTADISTICA, ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE')")
	public ResponseEntity<UIComponentDto> getEpidemiologicalWeekReport(@PathVariable(name = "institutionId") Integer institutionId){
		log.debug("Input parameter -> institutionId {}", institutionId);
		UIComponentDto result = JsonResourceUtils.readJson("extension/reports/epidemiologicalWeekReport.json",
				new TypeReference<>() {},
				null
		);
		return ResponseEntity.ok(result);
	}

	@GetMapping("/get-call-center-appointments")
	@PreAuthorize("hasAnyAuthority('GESTOR_CENTRO_LLAMADO')")
	public UIComponentDto getCallCenterAppointmentsReport() {
		return JsonResourceUtils.readJson("extension/reports/callCenterReport.json",
				new TypeReference<>() {},
				null
		);
	}

	@GetMapping("/institution/{institutionId}/reportes-estadisticos")
	@PreAuthorize("hasPermission(#institutionId, 'ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD, ESPECIALISTA_EN_ODONTOLOGIA, ENFERMERO, PERSONAL_DE_ESTADISTICA, ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE')")
	public ResponseEntity<UIComponentDto> reportesEstadisticos(@PathVariable(name = "institutionId") Integer institutionId){
		log.debug("Input parameter -> institutionId {}", institutionId);
		UIComponentDto result = JsonResourceUtils.readJson("extension/reports/reportes-estadisticos.json",
				new TypeReference<>() {},
				null
		);
		return ResponseEntity.ok(result);
	}
}
