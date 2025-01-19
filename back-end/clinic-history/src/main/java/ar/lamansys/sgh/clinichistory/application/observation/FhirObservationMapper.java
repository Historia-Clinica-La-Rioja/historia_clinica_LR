package ar.lamansys.sgh.clinichistory.application.observation;

import ar.lamansys.sgh.clinichistory.domain.ips.FhirObservationGroupBo;
import ar.lamansys.sgh.shared.infrastructure.input.service.observation.FhirObservationGroupInfoDto;
import ar.lamansys.sgx.shared.dates.configuration.LocalDateMapper;

import org.mapstruct.Mapper;
import org.mapstruct.Named;

@Mapper(uses = {LocalDateMapper.class})
public interface FhirObservationMapper {

	@Named("toFhirObservationGroupBo")
	FhirObservationGroupBo toFhirObservationGroupBo(FhirObservationGroupInfoDto fhirObservationGroupInfoDto);

}
