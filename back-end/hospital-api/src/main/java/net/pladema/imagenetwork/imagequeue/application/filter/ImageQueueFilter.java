package net.pladema.imagenetwork.imagequeue.application.filter;

import lombok.AllArgsConstructor;
import net.pladema.imagenetwork.imagequeue.domain.ImageQueueBo;
import net.pladema.imagenetwork.imagequeue.domain.ImageQueueFilteringCriteriaBo;

import java.text.Normalizer;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


@AllArgsConstructor
public class ImageQueueFilter {

    private final static Predicate<ImageQueueBo> ALLWAYS_TRUE = iq -> true;

    private ImageQueueFilteringCriteriaBo filteringCriteria;
    private Boolean isFFSelfPerceivedDataOn;

    public List<ImageQueueBo> byImageMoveAttributes(List<ImageQueueBo> unfilteredList){
        return unfilteredList.stream()
                .filter(predicateForEquipmentMatch().and(predicateForModalityMatch()).and(predicateForStatusMatch()))
                .collect(Collectors.toList());
    }

    public List<ImageQueueBo> byStudyName(List<ImageQueueBo> unfilteredList) {
        return unfilteredList.stream()
                .filter(predicateForStudyMatch())
                .collect(Collectors.toList());
    }

    public List<ImageQueueBo> byPatientData(List<ImageQueueBo> unfilteredList) {
        return unfilteredList.stream()
                .filter(predicateForNameMatch().and(predicateForIdentificationNumberMatch()))
                .collect(Collectors.toList());

    }

    private Predicate<ImageQueueBo> predicateForEquipmentMatch() {
        if (filteringCriteria.getEquipmentId().isEmpty())
            return ALLWAYS_TRUE;
        return iq -> filteringCriteria.getEquipmentId().get().equals(iq.getEquipmentId());
    }

    private Predicate<ImageQueueBo> predicateForModalityMatch() {
        if (filteringCriteria.getModalityId().isEmpty()) {
            return ALLWAYS_TRUE;
        }
        return iq -> filteringCriteria.getModalityId().get().equals(iq.getModalityId());
    }

    private Predicate<ImageQueueBo> predicateForStatusMatch() {
        if (filteringCriteria.getStatusList().isEmpty()) {
            return ALLWAYS_TRUE;
        }
        return iq -> filteringCriteria.getStatusList().contains(iq.getImageMoveStatus());
    }

    private Predicate<ImageQueueBo> predicateForStudyMatch() {
        if (filteringCriteria.getStudy().isEmpty()) {
            return ALLWAYS_TRUE;
        }
        return iq -> iq.getStudies().stream().anyMatch(
                studyName -> containsNormalized(studyName,filteringCriteria.getStudy().get())
        );
    }

    private Predicate<ImageQueueBo> predicateForNameMatch() {
        if (filteringCriteria.getName().isEmpty()) {
            return ALLWAYS_TRUE;
        }
        String name = filteringCriteria.getName().get();
        return iq -> (Objects.nonNull(iq.getPatientFirstName()) && containsNormalized(iq.getPatientFirstName(),name)) ||
                (Objects.nonNull(iq.getPatientLastName()) && containsNormalized(iq.getPatientLastName(),name)) ||
                (Objects.nonNull(iq.getPatientMiddleNames()) && containsNormalized(iq.getPatientMiddleNames(),name)) ||
                (Objects.nonNull(iq.getOtherLastNames()) && containsNormalized(iq.getOtherLastNames(),name)) ||
                (isFFSelfPerceivedDataOn && Objects.nonNull(iq.getNameSelfDetermination()) && containsNormalized(iq.getNameSelfDetermination(),name));
    }

    private Predicate<ImageQueueBo> predicateForIdentificationNumberMatch() {
        if (filteringCriteria.getIdentificationNumber().isEmpty()) {
            return ALLWAYS_TRUE;
        }
        return iq -> Objects.nonNull(iq.getPatientIdentificationNumber())
                && iq.getPatientIdentificationNumber().contains(filteringCriteria.getIdentificationNumber().get());
    }

    private String normalizeString(String input) {
        String normalized = Normalizer.normalize(input, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(normalized).replaceAll("").toUpperCase();
    }


    private Boolean containsNormalized(String str, String subStr) {
        return normalizeString(str).contains(normalizeString(subStr));
    }

}
