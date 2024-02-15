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

export interface DocumentType {
    title: string,
    id: number,
    icon: string
}