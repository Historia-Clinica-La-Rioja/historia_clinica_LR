package net.pladema.imagenetwork.imagequeue.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Setter
@NoArgsConstructor
@ToString
public class ImageQueueFilteringCriteriaBo {

    @Getter
    private LocalDate from;
    @Getter
    private LocalDate to;
    private Integer equipmentId;
    private Short modalityId;
    @Getter
    private List<EImageMoveStatus> statusList;
    private String name;
    private String identificationNumber;
    private String study;


    public Optional<Integer> getEquipmentId() {
        return Optional.ofNullable(equipmentId);
    }

    public Optional<Short> getModalityId() {
        return Optional.ofNullable(modalityId);
    }

    public Optional<String> getName() {
        return Optional.ofNullable(name);
    }

    public Optional<String> getIdentificationNumber() {
        return Optional.ofNullable(identificationNumber);
    }

    public Optional<String> getStudy() {
        return Optional.ofNullable(study);
    }
}
