package net.pladema.nomivac.infrastructure.output.immunization.repository;

import ar.lamansys.sgx.shared.dates.configuration.DateTimeProvider;
import net.pladema.nomivac.domain.immunization.ImmunizationSynchronizedInfoBo;
import net.pladema.nomivac.domain.immunization.UpdateSynchronizedStatus;
import org.springframework.stereotype.Service;

@Service
public class UpdateSynchronizedStatusImpl implements UpdateSynchronizedStatus {

    private final ImmunizationSyncRepository immunizationSyncRepository;

    private final DateTimeProvider dateTimeProvider;

    public UpdateSynchronizedStatusImpl(ImmunizationSyncRepository immunizationSyncRepository,
                                        DateTimeProvider dateTimeProvider) {
        this.immunizationSyncRepository = immunizationSyncRepository;
        this.dateTimeProvider = dateTimeProvider;
    }

    @Override
    public void run(ImmunizationSynchronizedInfoBo synchronizedInfoBo) {
        immunizationSyncRepository
                .findById(synchronizedInfoBo.getImmunizationId())
                .ifPresentOrElse(
                        nomivacImmunizationSyncS -> update(nomivacImmunizationSyncS, synchronizedInfoBo),
                        create(synchronizedInfoBo));

    }

    private Runnable create(ImmunizationSynchronizedInfoBo synchronizedInfoBo) {
        immunizationSyncRepository
                .saveAndFlush(new NomivacImmunizationSync(
                        synchronizedInfoBo.getImmunizationId(),
                        dateTimeProvider.nowDateTime(),
                        0,
                        synchronizedInfoBo.getExternalId(),
                        synchronizedInfoBo.getStatusCode()));
        return null;
    }

    private void update(NomivacImmunizationSync entitySync,
                                           ImmunizationSynchronizedInfoBo synchronizedInfoBo) {
        entitySync.updateOkStatus(synchronizedInfoBo.getExternalId());
        immunizationSyncRepository.saveAndFlush(entitySync);
    }

}
