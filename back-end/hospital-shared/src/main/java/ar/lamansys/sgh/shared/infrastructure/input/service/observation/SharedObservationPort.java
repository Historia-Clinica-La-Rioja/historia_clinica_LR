package ar.lamansys.sgh.shared.infrastructure.input.service.observation;


public interface SharedObservationPort {

	void save(FhirObservationGroupInfoDto fhirObservationGroupInfoDto);

}
