package net.pladema.emergencycare.controller;

import ar.lamansys.sgx.shared.masterdata.infrastructure.input.rest.dto.MasterDataDto;

import java.util.Collection;

public interface DischargeTypeMasterDataExternalService {

    Collection<MasterDataDto> internmentGetOf();

    Collection<MasterDataDto> emergencyCareGetOf();
}
