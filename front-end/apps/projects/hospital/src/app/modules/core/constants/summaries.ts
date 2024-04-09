import { EDocumentType } from "@api-rest/api-model";

export const DOCUMENT_TYPE: DocumentType[] = [
	{
		title: "Anamnesis",
		id: 1,
		icon: 'assignment_return'
	},
	{
		title: "Nota de evolución",
		id: 2,
		icon: 'assignment' //default
	},
	{
		title: "Epicrisis",
		id: 3,
		icon: 'assignment_turned_in'
	},
	{
		title: "Ambulatoria",
		id: 4,
		icon: 'report'
	},
	{
		title: "Receta",
		id: 5,
		icon: 'assignment' //default
	},
	{
		title: "Orden",
		id: 6,
		icon: 'assignment' //default
	},
	{
		title: "Guardia",
		id: 7,
		icon: 'local_hospital'
	},
	{
		title: "Inmunización",
		id: 8,
		icon: 'vaccines'
	},
	{
		title: "Odontología",
		id: 9,
		icon: 'assignment' //default
	},
	{
		title: "Enfermería",
		id: 10,
		icon: 'assignment' //default
	},
	{
		title: "Contrarreferencia",
		id: 11,
		icon: 'compare_arrows'
	},
	{
		title: "Indicación",
		id: 12,
		icon: 'assignment' //default
	},
	{
		title: "Nota de evolución de enfermería",
		id: 13,
		icon: 'assignment' //default
	},
	{
		title: "Receta digital",
		id: 14,
		icon: 'assignment' //default
	},
	{
		title: "Triage",
		id: 15,
		icon: 'list_alt'
	},
	{
		title: "Nota de evolución de guardia",
		id: 16,
		icon: 'assignment' //default
	},
	{
		title: "Reporte de imagen médica",
		id: 17,
		icon: 'assignment' //default
	},
];

export const getDocumentType = (id: number): DocumentType => {
	return DOCUMENT_TYPE.find(item => item.id == id);
}

export const getDocumentTypeByEnum = (type: string): DocumentType => {
	let documentType: DocumentType;
	switch (type) {
		case EDocumentType.ANAMNESIS:
			documentType = DOCUMENT_TYPE.find(item => item.id == 1);
			break;
		case EDocumentType.EVALUATION_NOTE:
			documentType = DOCUMENT_TYPE.find(item => item.id == 2);
			break;
		case EDocumentType.EPICRISIS:
			documentType = DOCUMENT_TYPE.find(item => item.id == 3);
			break;
		case EDocumentType.OUTPATIENT:
			documentType = DOCUMENT_TYPE.find(item => item.id == 4);
			break;
		case EDocumentType.RECIPE:
			documentType = DOCUMENT_TYPE.find(item => item.id == 5);
			break;
		case EDocumentType.ORDER:
			documentType = DOCUMENT_TYPE.find(item => item.id == 6);
			break;
		case EDocumentType.EMERGENCY_CARE:
			documentType = DOCUMENT_TYPE.find(item => item.id == 7);
			break;
		case EDocumentType.IMMUNIZATION:
			documentType = DOCUMENT_TYPE.find(item => item.id == 8);
			break;
		case EDocumentType.ODONTOLOGY:
			documentType = DOCUMENT_TYPE.find(item => item.id == 9);
			break;
		case EDocumentType.NURSING:
			documentType = DOCUMENT_TYPE.find(item => item.id == 10);
			break;
		case EDocumentType.COUNTER_REFERENCE:
			documentType = DOCUMENT_TYPE.find(item => item.id == 11);
			break;
		case EDocumentType.INDICATION:
			documentType = DOCUMENT_TYPE.find(item => item.id == 12);
			break;
		case EDocumentType.NURSING_EVOLUTION_NOTE:
			documentType = DOCUMENT_TYPE.find(item => item.id == 13);
			break;
		case EDocumentType.DIGITAL_RECIPE:
			documentType = DOCUMENT_TYPE.find(item => item.id == 14);
			break;
		case EDocumentType.TRIAGE:
			documentType = DOCUMENT_TYPE.find(item => item.id == 15);
			break;
		case EDocumentType.EMERGENCY_CARE_EVOLUTION:
			documentType = DOCUMENT_TYPE.find(item => item.id == 16);
			break;
		case EDocumentType.MEDICAL_IMAGE_REPORT:
			documentType = DOCUMENT_TYPE.find(item => item.id == 17);
	}
	return documentType;
}

// export const getDocumentTypeByEnum = (enum: string): DocumentType => {
// 	let documentType: DocumentType;
//     switch (enum) {
// 		case EDocumentType.ANAMNESIS:
// 			documentType = DOCUMENT_TYPE.find(item => item.id == 1);
// 			break;
// 	}
// 	return documentType;

// }

export interface DocumentType {
	title: string,
	id: number,
	icon: string
}
