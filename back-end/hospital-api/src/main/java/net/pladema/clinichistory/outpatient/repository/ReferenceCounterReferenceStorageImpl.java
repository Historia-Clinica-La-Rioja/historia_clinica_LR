package net.pladema.clinichistory.outpatient.repository;

import ar.lamansys.sgh.shared.infrastructure.input.service.SharedReferenceCounterReference;
import ar.lamansys.sgh.shared.infrastructure.input.service.referencecounterreference.CounterReferenceSummaryDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.clinichistory.outpatient.createoutpatient.service.domain.reference.CounterReferenceProcedureBo;
import net.pladema.clinichistory.outpatient.createoutpatient.service.domain.reference.CounterReferenceSummaryBo;
import net.pladema.clinichistory.outpatient.createoutpatient.service.domain.reference.ReferenceCounterReferenceFileBo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Slf4j
@Service
public class ReferenceCounterReferenceStorageImpl implements ReferenceCounterReferenceStorage {

    private final SharedReferenceCounterReference sharedReferenceCounterReference;

    @Override
    public CounterReferenceSummaryBo getCounterReference(Integer referenceId) {
        log.debug("Input parameter -> referenceId {}", referenceId);
        return sharedReferenceCounterReference.getCounterReference(referenceId)
				.map(this::mapToCounterReferenceSummaryBo)
				.orElse(new CounterReferenceSummaryBo());
    }

    @Override
    public List<ReferenceCounterReferenceFileBo> getReferenceFilesData(Integer referenceId) {
        log.debug("Input parameter -> referenceId {}", referenceId);
        return sharedReferenceCounterReference.getReferenceFilesData(referenceId)
                .stream()
                .map(rf -> new ReferenceCounterReferenceFileBo(rf.getFileId(), rf.getFileName()))
                .collect(Collectors.toList());
    }

    private CounterReferenceSummaryBo mapToCounterReferenceSummaryBo(CounterReferenceSummaryDto counterReferenceSummaryDto) {
        return new CounterReferenceSummaryBo(counterReferenceSummaryDto.getId(),
                counterReferenceSummaryDto.getClinicalSpecialty(),
                counterReferenceSummaryDto.getNote(),
                counterReferenceSummaryDto.getPerformedDate(),
                counterReferenceSummaryDto.getAuthorFullName(),
                counterReferenceSummaryDto.getFiles() != null ? counterReferenceSummaryDto.getFiles()
                        .stream()
                        .map(crf -> new ReferenceCounterReferenceFileBo(crf.getFileId(), crf.getFileName()))
                        .collect(Collectors.toList()) : null,
                counterReferenceSummaryDto.getProcedures() != null ? counterReferenceSummaryDto.getProcedures()
                        .stream()
                        .map(crp -> new CounterReferenceProcedureBo(crp.getSnomed().getSctid(), crp.getSnomed().getPt()))
                        .collect(Collectors.toList()) : null);
    }

}
