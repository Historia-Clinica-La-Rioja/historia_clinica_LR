import { ECHDocumentType, ECHEncounterType, CHDocumentSummaryDto } from "@api-rest/api-model";

export const ROUTE_HISTORY_CLINIC = 'ambulatoria/';

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

export const DocumentType = {
	[ECHDocumentType.CLINICAL_NOTES]: 'ambulatoria.print.document-type.CLINICAL_NOTES',
	[ECHDocumentType.EPICRISIS]: 'ambulatoria.print.document-type.EPICRISIS',
	[ECHDocumentType.MEDICAL_PRESCRIPTIONS]: 'ambulatoria.print.document-type.MEDICAL_PRESCRIPTIONS',
	[ECHDocumentType.REPORTS]: 'ambulatoria.print.document-type.REPORTS',
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
]


export const mockedTable: CHDocumentSummaryDto[] = [
	{
		id: 1,
		startDate: "20/1/2020",
		endDate: "5/12/20",
		encounterType: "Ambulatoria",
		problems: "Asma",
		institution: "Clinica Chacabuco",
		professional: "Carlos Montevideo"
	},
	{
		id: 2,
		startDate: "2/1/2018",
		endDate: "5/2/21",
		encounterType: "Internacion",
		problems: "Fiebre",
		institution: "Clinica Chacabuco",
		professional: "Carlos Montevideo"
	},
	{
		id: 3,
		startDate: "20/1/2020",
		endDate: "5/12/20",
		encounterType: "Ambulatoria",
		problems: "Gripe",
		institution: "Clinica Chacabuco",
		professional: "Carlos Montevideo"
	},
	{
		id: 4,
		startDate: "20/1/2020",
		endDate: "5/12/20",
		encounterType: "Internacion",
		problems: "Asma",
		institution: "Clinica Chacabuco",
		professional: "Carlos Montevideo"
	},
	{
		id: 5,
		startDate: "20/1/2020",
		endDate: "5/12/20",
		encounterType: "Ambulatoria",
		problems: "Asma",
		institution: "Clinica Chacabuco",
		professional: "Carlos Montevideo"
	},
	{
		id: 6,
		startDate: "20/1/2020",
		endDate: "5/12/20",
		encounterType: "Ambulatoria",
		problems: "Embarazada",
		institution: "Clinica Chacabuco",
		professional: "Carlos Montevideo"
	},
	{
		id: 7,
		startDate: "20/1/2020",
		endDate: "5/12/20",
		encounterType: "Ambulatoria",
		problems: "Embarazada",
		institution: "Clinica Chacabuco",
		professional: "Carlos Montevideo"
	},
	{
		id: 8,
		startDate: "20/1/2020",
		endDate: "5/12/20",
		encounterType: "Ambulatoria",
		problems: "Embarazada",
		institution: "Clinica Chacabuco",
		professional: "Carlos Montevideo"
	},
	{
		id: 9,
		startDate: "20/1/2020",
		endDate: "5/12/20",
		encounterType: "Ambulatoria",
		problems: "Embarazada",
		institution: "Clinica Chacabuco",
		professional: "Carlos Montevideo"
	},
	{
		id: 10,
		startDate: "20/1/2020",
		endDate: "5/12/20",
		encounterType: "Ambulatoria",
		problems: "Gastroenteritis",
		institution: "Clinica Chacabuco",
		professional: "Carlos Montevideo"
	},
	{
		id: 11,
		startDate: "20/1/2020",
		endDate: "5/12/20",
		encounterType: "Ambulatoria",
		problems: "Fiebre",
		institution: "Hospital Publico",
		professional: "Carlos Aragon"
	},
	{
		id: 12,
		startDate: "20/1/2020",
		endDate: "5/12/20",
		encounterType: "Internacion",
		problems: "Gripe",
		institution: "Clinica Chacabuco",
		professional: "Fernanda Calvo"
	},
]
