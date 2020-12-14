package net.pladema.clinichistory.requests.controller.dto;


import com.sun.istack.Nullable;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class PrescriptionDto implements Serializable {

    @NotNull
    private boolean hasRecipe;

    @Nullable
    private Integer medicalCoverageId;

    @NotEmpty
    private List<PrescriptionItemDto> items = new ArrayList();
}
