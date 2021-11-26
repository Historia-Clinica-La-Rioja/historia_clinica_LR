package net.pladema.snvs.application.ports.institution;

import net.pladema.snvs.domain.institution.InstitutionDataBo;

import java.util.Optional;

public interface InstitutionStorage {
    Optional<InstitutionDataBo> getInstitutionInfo(Integer institutionId);
}
