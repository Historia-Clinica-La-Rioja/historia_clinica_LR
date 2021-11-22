package net.pladema.nomivac.infrastructure.output.immunization.repository;

import ar.lamansys.sgx.shared.scheduling.infrastructure.output.service.AbstractPersistenceService;
import ar.lamansys.sgx.shared.scheduling.infrastructure.output.service.SyncErrorService;
import org.springframework.stereotype.Service;


@Service
public class ImmunizationPersistence extends AbstractPersistenceService<VNomivacImmunizationData, NomivacImmunizationSync, Integer> {

    public ImmunizationPersistence(SyncErrorService<NomivacImmunizationSync, Integer> syncErrorService,
                                   VNomivacImmunizationDataRepository VNomivacImmunizationDataRepository,
                                   NomivacImmunizationSyncRepository nomivacImmunizationSyncRepository) {
        super(syncErrorService, VNomivacImmunizationDataRepository, nomivacImmunizationSyncRepository, NomivacImmunizationSync::new);
    }
}
