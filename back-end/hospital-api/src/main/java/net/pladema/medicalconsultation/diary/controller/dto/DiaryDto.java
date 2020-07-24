package net.pladema.medicalconsultation.diary.controller.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString
public class DiaryDto extends DiaryADto {

    @NotNull
    private Integer id;
}
