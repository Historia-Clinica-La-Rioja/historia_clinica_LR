export enum REPORT_STATES_ID {
	COMPLETED = 1,
	DERIVED = 2,
	PENDING = 3,
	NOT_REQUIRED = 4
}

export enum REPORT_STATES_DESCRIPTION {
	COMPLETED = 'Informe completado',
	DERIVED = 'Informe derivado a ',
	PENDING = 'Informe pendiente',
	NOT_REQUIRED = 'Informe no requerido'
}

export const REPORT_STATES: ReportState[] = [
	{
		id: REPORT_STATES_ID.COMPLETED,
		description: REPORT_STATES_DESCRIPTION.COMPLETED,
        color: COLORES.COMPLETED
	},
    {
		id: REPORT_STATES_ID.DERIVED,
		description: REPORT_STATES_DESCRIPTION.DERIVED,
        color: COLORES.DERIVED
	},
    {
		id: REPORT_STATES_ID.PENDING,
		description: REPORT_STATES_DESCRIPTION.PENDING,
        color: COLORES.PENDING
	},
    {
		id: REPORT_STATES_ID.NOT_REQUIRED,
		description: REPORT_STATES_DESCRIPTION.NOT_REQUIRED,
        color: COLORES.NOT_REQUIRED
	}	
];

export interface ReportState {
	id: REPORT_STATES_ID;
	description: string;
    color: string;
}

export const enum COLORES {
    COMPLETED = 'green',
	DERIVED = 'mid-grey',
	PENDING = 'orange',
	NOT_REQUIRED = 'grey'
}
