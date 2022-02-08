package ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.service;

import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.dto.NewRiskFactorsObservationDto;
import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.dto.RiskFactorObservationDto;

public interface RiskFactorExternalService {

    NewRiskFactorsObservationDto saveRiskFactors(Integer patientId, NewRiskFactorsObservationDto riskFactorsObservationDto);

    RiskFactorObservationDto getRiskFactorObservationById(Integer riskFactorObservationId);

}
