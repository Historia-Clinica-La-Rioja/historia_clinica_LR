package net.pladema.establishment.application.bed;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.clinichistory.hospitalization.service.InternmentEpisodeService;
import net.pladema.establishment.application.bed.exceptions.BedRelocationException;
import net.pladema.establishment.domain.bed.BedRelocationBo;
import net.pladema.establishment.domain.bed.exceptions.BedRelocationEnumException;
import net.pladema.establishment.service.BedService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@RequiredArgsConstructor
@Service
public class DoBedRelocation {

    private final BedService bedService;
    private final InternmentEpisodeService internmentEpisodeService;

    public BedRelocationBo run(BedRelocationBo bedRelocationBo) {
        log.debug("Input parameters -> bedRelocationBo {}", bedRelocationBo);

        this.assertContextValid(bedRelocationBo);

        BedRelocationBo result = bedService.addPatientBedRelocation(bedRelocationBo);
        log.debug("Output -> {}", result);
        return result;
    }

    private void assertContextValid(BedRelocationBo bedRelocationBo) {
        boolean areEntryAndRelocateDatesValid = this.areEntryAndRelocateDatesValid(bedRelocationBo);
        if (!areEntryAndRelocateDatesValid) {
            throw new BedRelocationException(BedRelocationEnumException.ENTRY_DATE_AFTER_RELOCATION_DATE,
                    "La fecha de pase no puede ser inferior a la última fecha de pase registrada o fecha de ingreso");
        }

        boolean isFreeAndAvailableBed = bedService.isBedFreeAndAvailable(bedRelocationBo.getDestinationBedId());
        if (!isFreeAndAvailableBed) {
            throw new BedRelocationException(BedRelocationEnumException.OCUPPIED_BED,
                    "La cama seleccionada para asignar, ya fue ocupada");
        }
    }

    private boolean areEntryAndRelocateDatesValid(BedRelocationBo bedRelocationBo) {
        LocalDateTime entryDate = bedService.getLastPatientBedRelocation(bedRelocationBo.getInternmentEpisodeId())
                .map(BedRelocationBo::getRelocationDate)
                .orElse(internmentEpisodeService.getEntryDate(bedRelocationBo.getInternmentEpisodeId()));

        return entryDate.isBefore(bedRelocationBo.getRelocationDate());
    }
}