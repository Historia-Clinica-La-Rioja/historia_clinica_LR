package net.pladema.clinichistory.requests.controller.dto;


import com.sun.istack.Nullable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class PrescriptionDto implements Serializable {

    private boolean hasRecipe = false;

    @Nullable
    private Integer medicalCoverageId;

    @NotEmpty
    private List<@Valid PrescriptionItemDto> items = new ArrayList<>();
}
