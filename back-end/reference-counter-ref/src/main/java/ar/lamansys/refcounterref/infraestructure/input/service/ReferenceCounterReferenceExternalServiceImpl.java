package ar.lamansys.refcounterref.infraestructure.input.service;

import ar.lamansys.refcounterref.application.approvereferencesbyruleid.ApproveReferencesByRuleId;
import ar.lamansys.refcounterref.application.associatereferenceappointment.AssociateReferenceAppointment;
import ar.lamansys.refcounterref.application.createreference.CreateReference;
import ar.lamansys.refcounterref.application.getcounterreference.GetCounterReference;
import ar.lamansys.refcounterref.application.getreferencebyservicerequest.GetReferenceByServiceRequest;
import ar.lamansys.refcounterref.application.getreferencefile.GetReferenceFile;
import ar.lamansys.refcounterref.application.getreferenceproblem.GetReferenceProblem;
import ar.lamansys.refcounterref.application.updateruleonreferenceregulation.UpdateRuleOnReferenceRegulation;
import ar.lamansys.refcounterref.domain.counterreference.CounterReferenceSummaryBo;
import ar.lamansys.refcounterref.domain.file.ReferenceCounterReferenceFileBo;
import ar.lamansys.refcounterref.domain.procedure.CounterReferenceProcedureBo;
import ar.lamansys.refcounterref.domain.reference.ReferenceRequestBo;
import ar.lamansys.refcounterref.domain.referenceappointment.ReferenceAppointmentSummaryBo;
import ar.lamansys.refcounterref.infraestructure.input.service.mapper.CounterReferenceSummaryMapper;
import ar.lamansys.refcounterref.infraestructure.input.service.mapper.ReferenceMapper;
import ar.lamansys.refcounterref.infraestructure.input.service.mapper.ReferenceProblemMapper;
import ar.lamansys.refcounterref.infraestructure.output.repository.reference.ReferenceRepository;
import ar.lamansys.refcounterref.infraestructure.output.repository.referenceappointment.ReferenceAppointmentRepository;
import ar.lamansys.sgh.shared.domain.reference.ReferencePhoneBo;
import ar.lamansys.sgh.shared.infrastructure.input.service.SharedStaffPort;
import ar.lamansys.refcounterref.infraestructure.output.repository.referenceclinicalspecialty.ReferenceClinicalSpecialtyRepository;
import ar.lamansys.sgh.shared.infrastructure.input.service.referencecounterreference.CompleteReferenceDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.referencecounterreference.CounterReferenceSummaryDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.referencecounterreference.CounterReferenceSummaryProcedureDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.referencecounterreference.ReferenceAppointmentStateDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.referencecounterreference.ReferenceCounterReferenceFileDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.SharedReferenceCounterReference;
import ar.lamansys.sgh.shared.infrastructure.input.service.SharedSnomedDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.referencecounterreference.ReferencePhoneDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.referencecounterreference.ReferenceProblemDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.referencecounterreference.ReferenceRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
@Service
public class ReferenceCounterReferenceExternalServiceImpl implements SharedReferenceCounterReference {

	private final static Short ASSIGNED = 1;
	private final static Short CONFIRMED = 2;

    private final CreateReference createReference;
    private final GetReferenceFile getReferenceFile;
    private final GetCounterReference getCounterReference;
    private final GetReferenceProblem getReferenceProblem;
	private final AssociateReferenceAppointment associateReferenceAppointment;
    private final CounterReferenceSummaryMapper counterReferenceSummaryMapper;
    private final ReferenceMapper referenceMapper;
    private final ReferenceProblemMapper referenceProblemMapper;
    private final ReferenceAppointmentRepository referenceAppointmentRepository;
	private final GetReferenceByServiceRequest getReferenceByServiceRequest;
	private final SharedStaffPort sharedStaffPort;
	private final ApproveReferencesByRuleId approveReferencesByRuleId;
	private final UpdateRuleOnReferenceRegulation updateRuleIdOnReferences;
	private final ReferenceClinicalSpecialtyRepository referenceClinicalSpecialtyRepository;
	private final ReferenceRepository referenceRepository;

    @Override
    public List<ReferenceCounterReferenceFileDto> getReferenceFilesData(Integer referenceId) {
        log.debug("Input parameter -> referenceId {}", referenceId);
        List<ReferenceCounterReferenceFileBo> referenceCounterReferenceFileBos = getReferenceFile.getReferencesFileData(referenceId);
        return mapToReferenceCounterReferenceFileDto(referenceCounterReferenceFileBos);
    }

    @Override
    public Optional<CounterReferenceSummaryDto> getCounterReference(Integer referenceId) {
        log.debug("Input parameter -> referenceId {}", referenceId);
        Optional<CounterReferenceSummaryBo> counterReferenceSummaryBo = getCounterReference.run(referenceId);
		if (counterReferenceSummaryBo.isPresent()) {
			CounterReferenceSummaryBo cr = counterReferenceSummaryBo.get();
			CounterReferenceSummaryDto result = counterReferenceSummaryMapper.fromCounterReferenceSummaryBo(cr);
			result.setProcedures(mapToCounterReferenceSummaryProcedureDto(cr.getProcedures()));
			return Optional.of(result);
		}
        return Optional.empty();
    }

    @Override
    public List<Integer> saveReferences(List<CompleteReferenceDto> references) {
        log.debug("Input parameters -> references {}", references);
        return createReference.run(referenceMapper.fromCompleteReferenceDtoList(references));
    }

    @Override
    public List<ReferenceProblemDto> getReferencesProblemsByPatient(Integer patientId, List<Short> loggedUserRoleIds) {
        log.debug("Input parameters -> patientId {}, loggedUserRoleIds {}", patientId, loggedUserRoleIds);
        return referenceProblemMapper.fromReferenceProblemBoList(getReferenceProblem.run(patientId, loggedUserRoleIds));
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

	public boolean existsProtectedAppointmentInOpeningHour(Integer openingHourId) {
		log.debug("There are protected appointment in a opening hour with id {}", openingHourId);
		return referenceAppointmentRepository.existsInOpeningHour(openingHourId, Arrays.asList(ASSIGNED, CONFIRMED));
	}

	@Override
	public Optional<ReferenceRequestDto> getReferenceByServiceRequestId(Integer serviceRequestId){
		log.debug("Input parameter -> serviceRequestId {}", serviceRequestId);
		Optional<ReferenceRequestBo> reference = getReferenceByServiceRequest.run(serviceRequestId);
		Optional<ReferenceRequestDto> result = Optional.empty();
		if (reference.isPresent()) {
			result = Optional.of(referenceMapper.fromReferenceRequestBo(reference.get()));
			if (reference.get().getDoctorId() != null)
				result.get().setProfessionalInfo(sharedStaffPort.getProfessionalCompleteById(reference.get().getDoctorId()));
		}
		return result;
	}
	
	public Optional<ReferenceAppointmentStateDto> getReferenceByAppointmentId(Integer appointmentId){
		log.debug("Exists reference by appointmentId {}, ", appointmentId);
		var result = referenceAppointmentRepository.getReferenceByAppointmentId(appointmentId).map(this::mapToReferenceAppointmentStateDto);
		log.debug("OUTPUT {} ->", result);
		return result;
	}
	
	@Override
	public List<String> getReferenceClinicalSpecialtiesName(Integer referenceId) {
		log.debug("Input parameters -> referenceId {}", referenceId);
		List<String> result = referenceClinicalSpecialtyRepository.getClinicalSpecialtyNamesByReferenceId(referenceId);
		log.debug("Output -> {}", result);
		return result;
	}

	@Override
	public void approveReferencesByRuleId(Integer ruleId, List<Integer> institutionIds){
		log.debug("Input parameter -> ruleId {}", ruleId);
		approveReferencesByRuleId.run(ruleId, institutionIds);
	}

	@Override
	public void updateRuleOnReferences(Integer ruleId, Short ruleLevel, List<Integer> ruleIdsToReplace){
		log.debug("Input parameter -> ruleId {}, ruleLevel {}, ruleIdsToReplace {}", ruleId, ruleLevel, ruleIdsToReplace);
		updateRuleIdOnReferences.run(ruleId, ruleLevel, ruleIdsToReplace);
	}

	@Override
	public void updateProtectedAppointment(Integer appointmentId) {
		log.debug("Delete reference appointment {}, ", appointmentId);
		referenceAppointmentRepository.deleteByAppointmentId(appointmentId);
	}

	@Override
	public void associateReferenceToAppointment(Integer referenceId, Integer appointmentId, boolean isProtected, Integer institutionId) {
		log.debug("Associate reference to appointment {}, ", appointmentId);
		associateReferenceAppointment.run(referenceId, appointmentId, isProtected, institutionId);
	}

	@Override
	public ReferencePhoneDto getReferencePhoneData(Integer referenceId) {
		log.debug("Input parameter -> referenceId {} ", referenceId);
		return mapToReferencePhoneDto(referenceRepository.getReferencePhoneData(referenceId));
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

	private ReferenceAppointmentStateDto mapToReferenceAppointmentStateDto(ReferenceAppointmentSummaryBo referenceAppointmentSummaryBo) {
		ReferenceAppointmentStateDto result = new ReferenceAppointmentStateDto();
		result.setReferenceId(referenceAppointmentSummaryBo.getReferenceId());
		result.setAppointmentStateId(referenceAppointmentSummaryBo.getAppointmentStateId());
		result.setReferenceClosureTypeId(referenceAppointmentSummaryBo.getReferenceClosureTypeId());
		return result;
	}

	private ReferencePhoneDto mapToReferencePhoneDto(ReferencePhoneBo referencePhoneBo) {
		return new ReferencePhoneDto(referencePhoneBo.getPhonePrefix(), referencePhoneBo.getPhoneNumber());
	}

}
