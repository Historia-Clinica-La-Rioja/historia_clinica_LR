package net.pladema.nomivac.domain.immunization;

import java.util.Optional;

public interface FetchImmunizationToSynchronize {
    Optional<ImmunizationBo> run();
}
