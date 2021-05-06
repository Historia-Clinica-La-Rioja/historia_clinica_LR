package net.pladema.person.controller.service;

import net.pladema.person.controller.dto.EthnicityDto;

import java.util.List;

public interface PersonMasterDataExternalService {

    void updateActiveEthnicities(List<EthnicityDto> activeEthnicities);

}
