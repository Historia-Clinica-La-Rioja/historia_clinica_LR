export const DOCUMENT_TYPE: DocumentType[] = [
	{
		title: "Anamnesis",
		id: 1,
		icon: 'assignment_return'
	},
	{
		title: "Nota de evolución",
		id: 2,
		icon: 'assignment'
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
		icon: 'assignment'
	},
	{
		title: "Orden",
		id: 6,
		icon: 'assignment'
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
		icon: 'assignment'
	},
	{
		title: "Enfermería",
		id: 10,
		icon: 'assignment'
	},
	{
		title: "Contrarreferencia",
		id: 11,
		icon: 'compare_arrows'
	},
	{
		title: "Indicación",
		id: 12,
		icon: 'assignment'
	},
	{
		title: "Nota de evolución de enfermería",
		id: 13,
		icon: 'assignment'
	},
	{
		title: "Receta digital",
		id: 14,
		icon: 'assignment'
	},
	{
		title: "Triage",
		id: 15,
		icon: 'list_alt'
	},
	{
		title: "Nota de evolución de guardia",
		id: 16,
		icon: 'assignment'
	},
	{
		title: "Reporte de imagen médica",
		id: 17,
		icon: 'assignment'
	},
];

export const getDocumentType = (id: number): DocumentType => {
	return DOCUMENT_TYPE.find(item => item.id == id);
}
export interface DocumentType {
	title: string,
	id: number,
	icon: string
}
