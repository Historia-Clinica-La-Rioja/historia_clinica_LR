package ar.lamansys.odontology.infrastructure.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class OdontologyConceptDto implements Serializable {

    private OdontologySnomedDto snomed;

    private boolean applicableToTooth;

    private boolean applicableToSurface;

}
