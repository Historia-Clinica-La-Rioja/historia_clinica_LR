package net.pladema.clinichistory.requests.medicationrequests.controller.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ChangeStateMedicationRequestDto implements Serializable {

    @NotEmpty
    private List<Integer> medicationsIds = new ArrayList();
}
