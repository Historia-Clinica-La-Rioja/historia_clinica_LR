package net.pladema.imagenetwork.imagequeue.application.getimagequeue;

import ar.lamansys.sgh.shared.infrastructure.input.service.BasicPatientDto;
import ar.lamansys.sgx.shared.featureflags.AppFeature;
import ar.lamansys.sgx.shared.featureflags.application.FeatureFlagsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.clinichistory.requests.servicerequests.application.port.ServiceRequestStorage;
import net.pladema.imagenetwork.imagequeue.application.filter.ImageQueueFilter;
import net.pladema.imagenetwork.imagequeue.application.port.ImageQueueStorage;
import net.pladema.imagenetwork.imagequeue.domain.ImageQueueBo;
import net.pladema.imagenetwork.imagequeue.domain.ImageQueueFilteringCriteriaBo;
import net.pladema.imagenetwork.imagequeue.domain.ImageQueuePatientBo;
import net.pladema.patient.controller.service.PatientExternalService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class GetImageQueue {

    private final ImageQueueStorage imageQueueStorage;
    private final ServiceRequestStorage serviceRequestStorage;
    private final PatientExternalService patientExternalService;
    private final FeatureFlagsService featureFlagsService;

    public List<ImageQueueBo> run(
            Integer institutionId,
            ImageQueueFilteringCriteriaBo filteringCriteriaBo
    ) {
        log.debug("Input parameters -> institutionId {}, filteringCriteria {}", institutionId, filteringCriteriaBo);

        ImageQueueFilter filter = new ImageQueueFilter(
                filteringCriteriaBo,
                featureFlagsService.isOn(AppFeature.HABILITAR_DATOS_AUTOPERCIBIDOS)
        );

        List<ImageQueueBo> unfilteredList = imageQueueStorage.getStudiesInQueue(
                institutionId,
                filteringCriteriaBo.getFrom(),
                filteringCriteriaBo.getTo()
        );

        List<ImageQueueBo> filteredByEquipmentAndModalityAndStatus = filter.byImageMoveAttributes(unfilteredList);

        filteredByEquipmentAndModalityAndStatus.forEach(this::completeDiagnosticsReport);

        List<ImageQueueBo> filteredByStudyName = filter.byStudyName(filteredByEquipmentAndModalityAndStatus);

        completePatientData(filteredByStudyName);

        List<ImageQueueBo> finalFilteredList = filter.byPatientData(filteredByStudyName);

        log.debug("Output -> finalFilteredList {}",finalFilteredList);
        return finalFilteredList;
    }


    private void completeDiagnosticsReport(ImageQueueBo iq) {
        iq.setStudies(
                serviceRequestStorage.getDiagnosticReportsFrom(
                        iq.getStudyId(),
                        iq.getTranscribedServiceRequestId()
                )
        );
    }

    private void completePatientData(List<ImageQueueBo> resultService) {
        List<ImageQueueBo> imagesWithPatientId = resultService.stream()
                .filter(iqbo -> Objects.nonNull(iqbo.getPatientId()))
                .collect(Collectors.toList());

        Set<Integer> patientsIds = imagesWithPatientId.stream()
                .map(ImageQueueBo::getPatientId)
                .collect(Collectors.toSet());
        var basicPatientDtoMap = patientExternalService.getBasicDataFromPatientsId(patientsIds);
        imagesWithPatientId.forEach(iq ->
                iq.setPatient(
                        mapToImageQueuePatientData(basicPatientDtoMap.get(iq.getPatientId()))
                )
        );

    }

    private ImageQueuePatientBo mapToImageQueuePatientData(BasicPatientDto basicPatientDto) {
        return ImageQueuePatientBo.builder()
                .patientId(basicPatientDto.getId())
                .patientTypeId(basicPatientDto.getTypeId())
                .personId(basicPatientDto.getPerson().getId())
                .identificationType(basicPatientDto.getIdentificationType())
                .identificationNumber(basicPatientDto.getIdentificationNumber())
                .firstName(basicPatientDto.getFirstName())
                .middleNames(basicPatientDto.getMiddleName())
                .nameSelfDetermination(basicPatientDto.getPerson().getNameSelfDetermination())
                .lastName(basicPatientDto.getLastName())
                .otherLastNames(basicPatientDto.getPerson().getOtherLastNames())
                .gender(basicPatientDto.getGender())
                .selfPerceivedGender(basicPatientDto.getPerson().getSelfPerceivedGender())
                .personAgeDto(basicPatientDto.getPerson().getPersonAge())
                .birthDate(basicPatientDto.getBirthDate())
                .build();
    }
}
