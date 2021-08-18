package net.pladema.nomivac.domain.immunization;

public interface SendImmunization {
    ImmunizationSynchronizedInfoBo run(ImmunizationBo immunization);
}
