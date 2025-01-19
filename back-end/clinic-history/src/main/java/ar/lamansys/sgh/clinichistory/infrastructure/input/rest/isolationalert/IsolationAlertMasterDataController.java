package ar.lamansys.sgh.clinichistory.infrastructure.input.rest.isolationalert;

import ar.lamansys.sgh.clinichistory.domain.ips.enums.EIsolationStatus;
import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.isolationalert.dto.IsolationStatusMasterDataDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "Isolation alerts master data controller", description = "Isolation alerts master data controller")
@RequestMapping("/isolation-alerts/master-data")
@RequiredArgsConstructor
@Slf4j
@RestController
public class IsolationAlertMasterDataController {

	@GetMapping("/status")
	public List<IsolationStatusMasterDataDto> getStatus(){
		return EIsolationStatus
			.getAll()
			.stream()
			.map(status -> new IsolationStatusMasterDataDto(status.getId(), status.getDescription(), status.isOngoing()))
			.collect(Collectors.toList());
	}
}
