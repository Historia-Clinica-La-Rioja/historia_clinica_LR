import { ECHDocumentType, ECHEncounterType} from "@api-rest/api-model";

export const ROUTE_HISTORY_CLINIC = 'ambulatoria/';

export const EncounterType = {
	[ECHEncounterType.HOSPITALIZATION]: 'ambulatoria.print.encounter-type.HOSPITALIZATION',
	[ECHEncounterType.EMERGENCY_CARE]: 'ambulatoria.print.encounter-type.EMERGENCY_CARE',
	[ECHEncounterType.OUTPATIENT]: 'ambulatoria.print.encounter-type.OUTPATIENT',
}

export const EncounterTypes = [
	{
		value: ECHEncounterType.OUTPATIENT,
		label: EncounterType.OUTPATIENT
	},
	{
		value: ECHEncounterType.HOSPITALIZATION,
		label: EncounterType.HOSPITALIZATION
	},
	{
		value: ECHEncounterType.EMERGENCY_CARE,
		label: EncounterType.EMERGENCY_CARE
	}
]

export const DocumentType = {
	[ECHDocumentType.CLINICAL_NOTES]: 'ambulatoria.print.document-type.CLINICAL_NOTES',
	[ECHDocumentType.EPICRISIS]: 'ambulatoria.print.document-type.EPICRISIS',
	[ECHDocumentType.MEDICAL_PRESCRIPTIONS]: 'ambulatoria.print.document-type.MEDICAL_PRESCRIPTIONS',
	[ECHDocumentType.REPORTS]: 'ambulatoria.print.document-type.REPORTS',
	[ECHDocumentType.ANESTHETIC_REPORTS]: 'ambulatoria.print.document-type.ANESTHETIC_REPORTS',
}

export const DocumentTypes = [
	{
		value: ECHDocumentType.EPICRISIS,
		label: DocumentType.EPICRISIS
	},
	{
		value: ECHDocumentType.MEDICAL_PRESCRIPTIONS,
		label: DocumentType.MEDICAL_PRESCRIPTIONS
	},
	{
		value: ECHDocumentType.CLINICAL_NOTES,
		label: DocumentType.CLINICAL_NOTES
	},
	{
		value: ECHDocumentType.REPORTS,
		label: DocumentType.REPORTS
	},
	{
		value: ECHDocumentType.ANESTHETIC_REPORTS,
		label: DocumentType.ANESTHETIC_REPORTS
	},
]

export const TableColumns = ['select', 'startDate', 'endDate', 'encounterType', 'documentType', 'problem', 'institution', 'professional', 'download'];

