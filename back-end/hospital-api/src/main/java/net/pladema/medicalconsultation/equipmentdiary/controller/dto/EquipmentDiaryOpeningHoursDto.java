package net.pladema.medicalconsultation.equipmentdiary.controller.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.pladema.medicalconsultation.functional.Overlapping;

import javax.annotation.Nullable;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class EquipmentDiaryOpeningHoursDto implements Overlapping<EquipmentDiaryOpeningHoursDto> {

    @NotNull
    @Valid
    private EquipmentOpeningHoursDto openingHours;

    @NotNull
    private Short medicalAttentionTypeId;

    @Nullable
    private Short overturnCount = 0;

    private Boolean externalAppointmentsAllowed;

	private Boolean protectedAppointmentsAllowed;

	@Nullable
	private Boolean onSiteAttentionAllowed = true;

	@Nullable
	private Boolean regulationProtectedAppointmentsAllowed;

    @Override
    public boolean overlap(EquipmentDiaryOpeningHoursDto other) {
        return !(this.medicalAttentionTypeId.equals(other.getMedicalAttentionTypeId())) &&
                openingHours.overlap(other.getOpeningHours());

    }
}
