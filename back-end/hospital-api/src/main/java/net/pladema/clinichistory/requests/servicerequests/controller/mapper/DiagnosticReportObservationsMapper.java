package net.pladema.clinichistory.requests.servicerequests.controller.mapper;

import ar.lamansys.sgh.shared.infrastructure.input.service.servicerequest.dto.AddDiagnosticReportObservationCommandDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.servicerequest.dto.AddDiagnosticReportObservationsCommandDto;
import net.pladema.clinichistory.requests.servicerequests.controller.dto.observations.GetDiagnosticReportObservationDto;
import net.pladema.clinichistory.requests.servicerequests.controller.dto.observations.GetDiagnosticReportObservationGroupDto;
import net.pladema.clinichistory.requests.servicerequests.domain.observations.GetDiagnosticReportObservationGroupBo;
import ar.lamansys.sgh.shared.domain.servicerequest.SharedAddObservationsCommandVo;

import org.mapstruct.Mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Mapper
public interface DiagnosticReportObservationsMapper {

	SharedAddObservationsCommandVo fromDto(AddDiagnosticReportObservationsCommandDto dto);

	default SharedAddObservationsCommandVo.Observation fromDto(AddDiagnosticReportObservationCommandDto dto) {
		var ret = new SharedAddObservationsCommandVo.Observation();
		ret.setValue(dto.getValue());
		ret.setProcedureParameterId(dto.getProcedureParameterId());
		ret.setUnitOfMeasureId(dto.getUnitOfMeasureId());
		ret.setSnomedSctid(dto.getSnomedSctid());
		ret.setSnomedPt(dto.getSnomedPt());
		return ret;
	}

	default GetDiagnosticReportObservationGroupDto parseTo(GetDiagnosticReportObservationGroupBo bo) {
		List<GetDiagnosticReportObservationDto> observations = new ArrayList<>();
		if (bo.getObservations() != null)
			observations =
					bo.getObservations()
							.stream()
							.map(observation ->
								new GetDiagnosticReportObservationDto(
									observation.getId(),
									observation.getProcedureParameterId(),
									observation.getValue(),
									observation.getUnitOfMeasureId(),
									new GetDiagnosticReportObservationDto.Representation(
										observation.getRepresentation().getDescription(),
										observation.getRepresentation().getValue()
									),
									observation.getSnomedSctid(),
									observation.getSnomedPt())
							)
							.collect(Collectors.toList());
		return new GetDiagnosticReportObservationGroupDto(bo.getId(), bo.getDiagnosticReportId(), bo.getProcedureTemplateId(), bo.getIsPartialUpload(), observations);
	}
}
