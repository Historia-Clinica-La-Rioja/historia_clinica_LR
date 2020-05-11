package net.pladema.internation.controller.documents.epicrisis.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.annotation.Nullable;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Getter
@Setter
@ToString
public class NewEpicrisisDto implements Serializable {

    @NotNull
    private boolean confirmed = false;

    @Nullable
    private EpicrisisObservationsDto notes;

}
