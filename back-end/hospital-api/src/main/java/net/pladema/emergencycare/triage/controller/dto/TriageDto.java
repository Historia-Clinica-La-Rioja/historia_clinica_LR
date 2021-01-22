package net.pladema.emergencycare.triage.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.annotation.Nullable;
import java.io.Serializable;

@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public abstract class TriageDto implements Serializable {

    Short categoryId;

    @Nullable
    Integer doctorsOfficeId;

}
