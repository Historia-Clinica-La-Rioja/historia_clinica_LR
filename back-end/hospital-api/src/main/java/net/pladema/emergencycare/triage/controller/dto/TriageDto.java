package net.pladema.emergencycare.triage.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import net.pladema.clinichistory.outpatient.createoutpatient.controller.dto.OutpatientReasonDto;

import javax.annotation.Nullable;
import java.io.Serializable;
import java.util.List;

@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public abstract class TriageDto implements Serializable {

    Short categoryId;

    @Nullable
    Integer doctorsOfficeId;

	@Nullable
	List<OutpatientReasonDto> reasons;

	@Nullable
	Integer clinicalSpecialtySectorId;
}
