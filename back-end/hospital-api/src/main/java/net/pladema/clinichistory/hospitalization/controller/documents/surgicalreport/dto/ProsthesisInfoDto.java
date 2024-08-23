package net.pladema.clinichistory.hospitalization.controller.documents.surgicalreport.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.annotation.Nullable;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class ProsthesisInfoDto {

    private Boolean hasProsthesis;

    @Nullable
    private String description;
}
