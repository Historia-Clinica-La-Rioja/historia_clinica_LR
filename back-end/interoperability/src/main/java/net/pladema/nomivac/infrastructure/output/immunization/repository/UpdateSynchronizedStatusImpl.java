package net.pladema.nomivac.infrastructure.output.immunization.repository;

import ar.lamansys.sgx.shared.dates.configuration.DateTimeProvider;
import ar.lamansys.sgx.shared.scheduling.infrastructure.output.service.ESyncError;
import ar.lamansys.sgx.shared.scheduling.infrastructure.output.service.SyncErrorService;
import net.pladema.nomivac.domain.immunization.ImmunizationSynchronizedInfoBo;
import net.pladema.nomivac.domain.immunization.UpdateSynchronizedStatus;
import net.pladema.nomivac.infrastructure.configuration.NomivacCondition;
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Service;

@Service
@Conditional(NomivacCondition.class)
public class UpdateSynchronizedStatusImpl implements UpdateSynchronizedStatus {

    private final NomivacImmunizationSyncRepository nomivacImmunizationSyncRepository;

    private final DateTimeProvider dateTimeProvider;

    private final SyncErrorService<NomivacImmunizationSync, Integer> syncErrorService;

    public UpdateSynchronizedStatusImpl(NomivacImmunizationSyncRepository nomivacImmunizationSyncRepository,
                                        DateTimeProvider dateTimeProvider,
                                        SyncErrorService<NomivacImmunizationSync, Integer> syncErrorService) {
        this.nomivacImmunizationSyncRepository = nomivacImmunizationSyncRepository;
        this.dateTimeProvider = dateTimeProvider;
        this.syncErrorService = syncErrorService;
    }

    @Override
    public void run(ImmunizationSynchronizedInfoBo synchronizedInfoBo) {
        nomivacImmunizationSyncRepository
                .findById(synchronizedInfoBo.getImmunizationId())
                .ifPresentOrElse(
                        nomivacImmunizationSyncS -> update(nomivacImmunizationSyncS, synchronizedInfoBo),
                        () -> create(synchronizedInfoBo));

    }

    private void create(ImmunizationSynchronizedInfoBo synchronizedInfoBo) {
        var nomivacSync = new NomivacImmunizationSync(synchronizedInfoBo.getImmunizationId(),
                dateTimeProvider.nowDateTime(),
                0,
                synchronizedInfoBo.getExternalId(),
                synchronizedInfoBo.getStatusCode());
        nomivacImmunizationSyncRepository.saveAndFlush(nomivacSync);
        if (synchronizedInfoBo.isUnsuccessfullyOperation())
            syncErrorService.createError(nomivacSync, ESyncError.IMMUNIZATION, synchronizedInfoBo.getMessage());
    }

    private void update(NomivacImmunizationSync entitySync,
                                           ImmunizationSynchronizedInfoBo synchronizedInfoBo) {
        entitySync.updateOkStatus(synchronizedInfoBo.getExternalId());
        nomivacImmunizationSyncRepository.saveAndFlush(entitySync);
        if (synchronizedInfoBo.isUnsuccessfullyOperation())
            syncErrorService.createError(entitySync, ESyncError.IMMUNIZATION, synchronizedInfoBo.getMessage());
    }

}
