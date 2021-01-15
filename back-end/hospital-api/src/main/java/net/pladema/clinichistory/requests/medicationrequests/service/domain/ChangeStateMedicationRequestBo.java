package net.pladema.clinichistory.requests.medicationrequests.service.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;
import java.util.List;

@AllArgsConstructor
@Getter
public class ChangeStateMedicationRequestBo implements Serializable {

    private final String statusId;

    private final Double dayQuantity;

    private final String observations;

    private final List<Integer> medicationsIds;
}
