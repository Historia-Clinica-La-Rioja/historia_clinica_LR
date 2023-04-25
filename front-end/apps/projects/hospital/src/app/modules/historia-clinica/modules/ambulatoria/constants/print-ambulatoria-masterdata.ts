import { ECHEncounterType } from "@api-rest/api-model";

export const EncounterType = {
	[ECHEncounterType.HOSPITALIZATION]: 'ambulatoria.print.encounter-type.HOSPITALIZATION',
	[ECHEncounterType.EMERGENCY_CARE]: 'ambulatoria.print.encounter-type.EMERGENCY_CARE',
	[ECHEncounterType.OUTPATIENT]: 'ambulatoria.print.encounter-type.OUTPATIENT',
}

export const EncounterTypes = [
	{
		value: ECHEncounterType.EMERGENCY_CARE,
		label: EncounterType.EMERGENCY_CARE
	},
	{
		value: ECHEncounterType.HOSPITALIZATION,
		label: EncounterType.HOSPITALIZATION
	}
]
