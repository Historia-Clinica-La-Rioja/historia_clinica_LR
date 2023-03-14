package ar.lamansys.refcounterref.infraestructure.input.service;

import ar.lamansys.refcounterref.application.createreference.CreateReference;
import ar.lamansys.refcounterref.application.getcounterreference.GetCounterReference;
import ar.lamansys.refcounterref.application.getreferencefile.GetReferenceFile;
import ar.lamansys.refcounterref.application.getreferenceproblem.GetReferenceProblem;
import ar.lamansys.refcounterref.domain.counterreference.CounterReferenceSummaryBo;
import ar.lamansys.refcounterref.domain.file.ReferenceCounterReferenceFileBo;
import ar.lamansys.refcounterref.domain.procedure.CounterReferenceProcedureBo;
import ar.lamansys.refcounterref.infraestructure.input.service.mapper.CounterReferenceSummaryMapper;
import ar.lamansys.refcounterref.infraestructure.input.service.mapper.ReferenceMapper;
import ar.lamansys.refcounterref.infraestructure.input.service.mapper.ReferenceProblemMapper;
import ar.lamansys.refcounterref.infraestructure.output.repository.referenceappointment.ReferenceAppointmentPk;
import ar.lamansys.refcounterref.infraestructure.output.repository.referenceappointment.ReferenceAppointmentRepository;
import ar.lamansys.sgh.shared.infrastructure.input.service.referencecounterreference.CounterReferenceSummaryDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.referencecounterreference.CounterReferenceSummaryProcedureDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.referencecounterreference.ReferenceCounterReferenceFileDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.SharedReferenceCounterReference;
import ar.lamansys.sgh.shared.infrastructure.input.service.SharedSnomedDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.referencecounterreference.ReferenceDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.referencecounterreference.ReferenceProblemDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
@Service
public class ReferenceCounterReferenceExternalServiceImpl implements SharedReferenceCounterReference {

    private final CreateReference createReference;
    private final GetReferenceFile getReferenceFile;
    private final GetCounterReference getCounterReference;
    private final GetReferenceProblem getReferenceProblem;
    private final CounterReferenceSummaryMapper counterReferenceSummaryMapper;
    private final ReferenceMapper referenceMapper;
    private final ReferenceProblemMapper referenceProblemMapper;
    private final ReferenceAppointmentRepository referenceAppointmentRepository;

    @Override
    public List<ReferenceCounterReferenceFileDto> getReferenceFilesData(Integer referenceId) {
        log.debug("Input parameter -> referenceId {}", referenceId);
        List<ReferenceCounterReferenceFileBo> referenceCounterReferenceFileBos = getReferenceFile.getReferencesFileData(referenceId);
        return mapToReferenceCounterReferenceFileDto(referenceCounterReferenceFileBos);
    }

    @Override
    public CounterReferenceSummaryDto getCounterReference(Integer referenceId) {
        log.debug("Input parameter -> referenceId {}", referenceId);
        CounterReferenceSummaryBo counterReferenceSummaryBo = getCounterReference.run(referenceId);
        CounterReferenceSummaryDto result = counterReferenceSummaryMapper.fromCounterReferenceSummaryBo(counterReferenceSummaryBo);
        if(result.getId() != null)
            result.setProcedures(mapToCounterReferenceSummaryProcedureDto(counterReferenceSummaryBo.getProcedures()));
        return result;
    }

    @Override
    public void saveReferences(Integer encounterId, Integer sourceTypeId, List<ReferenceDto> refrenceDtoList) {
        log.debug("Input parameters -> encounterId {}, sourceTypeId {}, referenceDtoList {}", encounterId, sourceTypeId, refrenceDtoList);
        createReference.run(encounterId, sourceTypeId, referenceMapper.fromReferenceDtoList(refrenceDtoList));
    }

    @Override
    public List<ReferenceProblemDto> getReferencesProblemsByPatient(Integer patientId) {
        log.debug("Input parameters -> patientId {} ", patientId);
        return referenceProblemMapper.fromReferenceProblemBoList(getReferenceProblem.run(patientId));
    }

	@Override
	public Integer getAssignedProtectedAppointmentsQuantity(Integer diaryId, LocalDate day, Short appointmentStateId) {
		log.debug("Input parameters -> diaryId {}, day {}, appointmentStateId {}", diaryId, day, appointmentStateId);
        return referenceAppointmentRepository.getAssignedProtectedAppointmentsQuantity(diaryId, day, appointmentStateId);
	}

	@Override
	public List<Integer> getProtectedAppointmentsIds(List<Integer> diaryIds) {
		log.debug("Input parameter -> diaryIds {}", diaryIds);
		return referenceAppointmentRepository.findAppointmentIdsByDiaryIds(diaryIds);
	}

	@Override
	public boolean isProtectedAppointment(Integer appointmentId) {
		return referenceAppointmentRepository.isProtectedAppointment(appointmentId);
	}

	@Override
	public void updateProtectedAppointment(Integer appointmentId) {
    	log.debug("Delete reference appointment {}, ", appointmentId);
    	referenceAppointmentRepository.deleteByAppointmentId(appointmentId);
	}

	private List<ReferenceCounterReferenceFileDto> mapToReferenceCounterReferenceFileDto(List<ReferenceCounterReferenceFileBo> referenceCounterReferenceFileBos) {
        List<ReferenceCounterReferenceFileDto> referenceCounterReferenceFileDtos = new ArrayList<>();
        referenceCounterReferenceFileBos.stream().forEach(referenceCounterReferenceFileBo ->
                referenceCounterReferenceFileDtos.add(new ReferenceCounterReferenceFileDto(referenceCounterReferenceFileBo.getFileId(), referenceCounterReferenceFileBo.getFileName()))
        );
        return referenceCounterReferenceFileDtos;
    }

    private List<CounterReferenceSummaryProcedureDto> mapToCounterReferenceSummaryProcedureDto(List<CounterReferenceProcedureBo> counterReferenceProcedureBoList) {
        List<CounterReferenceSummaryProcedureDto> counterReferenceSummaryProcedureDtoList = new ArrayList<>();
        counterReferenceProcedureBoList.stream().forEach(counterReferenceProcedureBo ->
                counterReferenceSummaryProcedureDtoList.add(new CounterReferenceSummaryProcedureDto(new SharedSnomedDto(counterReferenceProcedureBo.getSctid(), counterReferenceProcedureBo.getPt()))));
        return counterReferenceSummaryProcedureDtoList;
    }

}
