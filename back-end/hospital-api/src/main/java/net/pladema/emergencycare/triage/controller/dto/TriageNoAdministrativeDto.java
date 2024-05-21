package net.pladema.emergencycare.triage.controller.dto;

import lombok.Getter;
import lombok.ToString;
import net.pladema.clinichistory.outpatient.createoutpatient.controller.dto.OutpatientReasonDto;

import javax.annotation.Nullable;

import java.util.List;

@Getter
@ToString
public abstract class TriageNoAdministrativeDto extends TriageDto {

    @Nullable
    String notes;

	@Nullable
	List<OutpatientReasonDto> reasons;

    public TriageNoAdministrativeDto(Short categoryId, Integer doctorsOfficeId, String notes, List<OutpatientReasonDto> reasons){
        super(categoryId, doctorsOfficeId);
        this.notes = notes;
		this.reasons = reasons;
    }

    public TriageNoAdministrativeDto() { }
}
