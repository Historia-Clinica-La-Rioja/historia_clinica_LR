package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.hce.summary;

import ar.lamansys.sgh.clinichistory.application.ports.HCEReferenceCounterReferenceStorage;
import ar.lamansys.sgh.clinichistory.domain.hce.HCEReferenceProblemBo;
import ar.lamansys.sgh.clinichistory.domain.hce.summary.CounterReferenceProcedureBo;
import ar.lamansys.sgh.clinichistory.domain.hce.summary.CounterReferenceSummaryBo;
import ar.lamansys.sgh.clinichistory.domain.hce.summary.ReferenceCounterReferenceFileBo;
import ar.lamansys.sgh.clinichistory.domain.ips.SnomedBo;
import ar.lamansys.sgh.shared.infrastructure.input.service.SharedReferenceCounterReference;
import ar.lamansys.sgh.shared.infrastructure.input.service.referencecounterreference.CounterReferenceSummaryDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.referencecounterreference.ReferenceProblemDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class HCEReferenceCounterReferenceStorageImpl implements HCEReferenceCounterReferenceStorage {

    private final SharedReferenceCounterReference sharedReferenceCounterReference;

    public HCEReferenceCounterReferenceStorageImpl(SharedReferenceCounterReference sharedReferenceCounterReference) {
        this.sharedReferenceCounterReference = sharedReferenceCounterReference;
    }

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

    @Override
    public List<HCEReferenceProblemBo> getProblemsWithReferences(Integer patientId, List<Short> loggedUserRoleIds) {
        log.debug("Input parameters -> patientId {}, loggedUserRoleIds {}", patientId, loggedUserRoleIds);
        return mapToHCEReferenceProblemBoList(sharedReferenceCounterReference.getReferencesProblemsByPatient(patientId, loggedUserRoleIds));
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
                        .collect(Collectors.toList()) : null,
				counterReferenceSummaryDto.getInstitution(),
				counterReferenceSummaryDto.getClosureType());

    }

    private List<HCEReferenceProblemBo> mapToHCEReferenceProblemBoList(List<ReferenceProblemDto> referenceProblemDtoList) {
        List<HCEReferenceProblemBo> hceReferenceProblemBoList = new ArrayList<>();
        referenceProblemDtoList.stream().forEach(referenceProblemDto -> {
            hceReferenceProblemBoList.add(new HCEReferenceProblemBo(referenceProblemDto.getId(),
                    new SnomedBo(referenceProblemDto.getSnomed().getSctid(), referenceProblemDto.getSnomed().getPt())));
        });
        return hceReferenceProblemBoList;
    }

}
