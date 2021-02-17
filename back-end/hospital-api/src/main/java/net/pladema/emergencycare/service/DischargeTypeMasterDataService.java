package net.pladema.emergencycare.service;

import net.pladema.emergencycare.service.domain.DischargeTypeBo;

import java.util.List;

public interface DischargeTypeMasterDataService {

    List<DischargeTypeBo> getAllInternmentDischargeTypes();

    List<DischargeTypeBo> getAllEmergencyCareDischargeTypes();

}
