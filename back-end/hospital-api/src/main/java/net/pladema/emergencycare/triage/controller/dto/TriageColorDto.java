package net.pladema.emergencycare.triage.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Value;

import java.io.Serializable;

@Builder
@Value
@Getter
@AllArgsConstructor
public class TriageColorDto implements Serializable {

    String name;

    String code;

}
