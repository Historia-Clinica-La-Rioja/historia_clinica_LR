package ar.lamansys.odontology.infrastructure.controller.consultation.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

@Getter
@NoArgsConstructor
@ToString
public enum ESurfacePositionDto implements Serializable {
    INTERNAL("internal"),
    EXTERNAL("external"),
    LEFT("left"),
    RIGHT("right"),
    CENTRAL("central");

    private String value;

    ESurfacePositionDto(String value) {
        this.value = value;
    }
}
