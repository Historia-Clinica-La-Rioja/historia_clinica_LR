package net.pladema.snvs.infrastructure.output.repository.institution;

import ar.lamansys.sgh.shared.infrastructure.input.service.institution.InstitutionInfoDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.institution.SharedInstitutionPort;
import net.pladema.snvs.application.ports.institution.InstitutionStorage;
import net.pladema.snvs.domain.institution.InstitutionDataBo;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class InstitutionStorageImpl implements InstitutionStorage {

    private final SharedInstitutionPort sharedInstitutionPort;

    public InstitutionStorageImpl(SharedInstitutionPort sharedInstitutionPort) {
        this.sharedInstitutionPort = sharedInstitutionPort;
    }

    @Override
    public Optional<InstitutionDataBo> getInstitutionInfo(Integer institutionId) {
        return Optional.of(sharedInstitutionPort.fetchInstitutionById(institutionId))
                .map(this::mapData);
    }

    private InstitutionDataBo mapData(InstitutionInfoDto institutionInfoDto) {
        return new InstitutionDataBo(institutionInfoDto.getId(), institutionInfoDto.getSisaCode());
    }
}
