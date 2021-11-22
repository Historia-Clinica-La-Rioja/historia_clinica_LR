package net.pladema.nomivac.infrastructure.output.immunization.rest;

import net.pladema.nomivac.domain.immunization.ImmunizationBo;
import net.pladema.nomivac.domain.immunization.ImmunizationSynchronizedInfoBo;
import net.pladema.nomivac.domain.immunization.SendImmunization;
import net.pladema.nomivac.infrastructure.configuration.NomivacCondition;
import org.hl7.fhir.r4.model.Immunization;
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Service;

@Service
@Conditional(NomivacCondition.class)
public class SendImmunizationImpl implements SendImmunization {

    private final NomivacWebService nomivacWebService;

    private final ImmunizationNomivacResourceMapper immunizationNomivacResourceMapper;

    public SendImmunizationImpl(NomivacWebService nomivacWebService,
                                ImmunizationNomivacResourceMapper immunizationNomivacResourceMapper) {
        this.nomivacWebService = nomivacWebService;
        this.immunizationNomivacResourceMapper = immunizationNomivacResourceMapper;
    }


    @Override
    public ImmunizationSynchronizedInfoBo run(ImmunizationBo immunizationBo) {
        Immunization immunization = immunizationNomivacResourceMapper.map(immunizationBo);
        NomivacImmunizationPostResponse response = nomivacWebService.postImmunization(immunization);
        return new ImmunizationSynchronizedInfoBo(immunizationBo.getId(), response.getNomivacId(),
                response.getStatusCode(), response.getMessage(), response.isUnsuccessfullyOperation());
    }

}
