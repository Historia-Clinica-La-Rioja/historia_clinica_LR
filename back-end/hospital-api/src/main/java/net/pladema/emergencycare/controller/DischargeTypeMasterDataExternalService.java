package net.pladema.emergencycare.controller;

import net.pladema.sgx.masterdata.dto.MasterDataDto;

import java.util.Collection;

public interface DischargeTypeMasterDataExternalService {

    Collection<MasterDataDto> internmentGetOf();

    Collection<MasterDataDto> emergencyCareGetOf();
}
