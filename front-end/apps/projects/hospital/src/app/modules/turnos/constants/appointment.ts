import { EAppointmentModality } from "@api-rest/api-model";
import { Color } from "@presentation/colored-label/colored-label.component";

export const APPOINTMENT_DURATIONS = [
	{
		id: 10,
		description: '10 minutos'
	},
	{
		id: 15,
		description: '15 minutos'
	},
	{
		id: 20,
		description: '20 minutos'
	},
	{
		id: 30,
		description: '30 minutos'
	},
	{
		id: 60,
		description: '60 minutos'
	}
];

export const CANCEL_STATE_ID = 4;

export enum APPOINTMENT_STATES_ID {
	ASSIGNED = 1,
	CONFIRMED = 2,
	ABSENT = 3,
	CANCELLED = 4,
	SERVED = 5,
	BOOKED = 6,
	BLOCKED = 7,
	OUT_OF_DIARY = 8
}

export enum APPOINTMENT_STATES_DESCRIPTION {
	ASSIGNED = 'Asignado',
	CONFIRMED = 'Confirmado',
	ABSENT = 'Ausente',
	CANCELLED = 'Cancelado',
	SERVED = 'Atendido',
	BOOKED = 'Reserva',
	OUT_OF_DIARY = 'Fuera de agenda',
	CONFIRMED_WAITING_ROOM = 'En sala'
}

export const APPOINTMENT_STATES: AppointmentState[] = [
	{
		id: APPOINTMENT_STATES_ID.ASSIGNED,
		description: APPOINTMENT_STATES_DESCRIPTION.ASSIGNED
	},
	{
		id: APPOINTMENT_STATES_ID.CONFIRMED,
		description: APPOINTMENT_STATES_DESCRIPTION.CONFIRMED
	},
	{
		id: APPOINTMENT_STATES_ID.ABSENT,
		description: APPOINTMENT_STATES_DESCRIPTION.ABSENT
	},
	{
		id: APPOINTMENT_STATES_ID.CANCELLED,
		description: APPOINTMENT_STATES_DESCRIPTION.CANCELLED
	},
	{
		id: APPOINTMENT_STATES_ID.SERVED,
		description: APPOINTMENT_STATES_DESCRIPTION.SERVED
	},
	{
		id: APPOINTMENT_STATES_ID.BOOKED,
		description: APPOINTMENT_STATES_DESCRIPTION.BOOKED
	},
	{
		id: APPOINTMENT_STATES_ID.OUT_OF_DIARY,
		description: APPOINTMENT_STATES_DESCRIPTION.OUT_OF_DIARY
	},

];

export const WORKLIST_APPOINTMENT_STATES: AppointmentState[] = [
	{
		id: APPOINTMENT_STATES_ID.ASSIGNED,
		description: APPOINTMENT_STATES_DESCRIPTION.ASSIGNED
	},
	{
		id: APPOINTMENT_STATES_ID.CONFIRMED,
		description: APPOINTMENT_STATES_DESCRIPTION.CONFIRMED_WAITING_ROOM
	},
	{
		id: APPOINTMENT_STATES_ID.ABSENT,
		description: APPOINTMENT_STATES_DESCRIPTION.ABSENT
	},
	{
		id: APPOINTMENT_STATES_ID.SERVED,
		description: APPOINTMENT_STATES_DESCRIPTION.SERVED
	},
	// {
	// 	id: APPOINTMENT_STATES_ID.CANCELLED,
	// 	description: 'Rechazado'
	// }
];

export interface AppointmentState {
	id: APPOINTMENT_STATES_ID;
	description: string;
}

export function getAppointmentState(id: APPOINTMENT_STATES_ID): AppointmentState {
	return APPOINTMENT_STATES.find(appointment => appointment.id === id);
}

export const MAX_LENGTH_MOTIVE = 255;

export const MINUTES_IN_HOUR = 60;

export const TEMPORARY_PATIENT = 3;
export const GREY_TEXT = 'calendar-event-grey-text';
export const WHITE_TEXT = 'calendar-event-white-text';
export const BLUE_TEXT = 'calendar-event-blue-text';
export const PURPLE_TEXT = 'calendar-event-purple-text';

export const enum COLORES {
	ASSIGNED = '#4187FF',
	CONFIRMED = '#FFA500',
	ABSENT = '#D5E0D5',
	BLOCKED = '#7D807D',
	SERVED = '#A3EBAF',
	PROGRAMADA = '#7FC681',
	ESPONTANEA = '#2687C5',
	SOBRETURNO = '#1A45DD',
	RESERVA_ALTA = '#FFFFFF',
	RESERVA_VALIDACION = '#EB5757',
	FUERA_DE_AGENDA = '#FF0000',
	PROTECTED = '#AF26C5',
	CANCELLED = '#F04848,'
}

export const MODALITYS = {
		[EAppointmentModality.ON_SITE_ATTENTION]: 'turnos.ON_SITE_ATTENTION',
		[EAppointmentModality.PATIENT_VIRTUAL_ATTENTION]: 'turnos.PATIENT_VIRTUAL_ATTENTION',
		[EAppointmentModality.SECOND_OPINION_VIRTUAL_ATTENTION]: 'turnos.SECOND_OPINION_VIRTUAL_ATTENTION'
}

export const stateColor = {
    [APPOINTMENT_STATES_ID.CONFIRMED]:  Color.YELLOW,
    [APPOINTMENT_STATES_ID.ABSENT]: Color.GREY,
    [APPOINTMENT_STATES_ID.SERVED]: Color.GREEN,
    [APPOINTMENT_STATES_ID.CANCELLED]: Color.RED,
    [APPOINTMENT_STATES_ID.ASSIGNED]: Color.BLUE,
}
export interface modality {
	value: EAppointmentModality;
	description: string;
}

export const MODALITYS_TYPES : modality [] = [
	{
		value: EAppointmentModality.ON_SITE_ATTENTION,
		description: MODALITYS[EAppointmentModality.ON_SITE_ATTENTION]
	},
	{
		value: EAppointmentModality.PATIENT_VIRTUAL_ATTENTION,
		description: MODALITYS[EAppointmentModality.PATIENT_VIRTUAL_ATTENTION]
	},
	{
		value: EAppointmentModality.SECOND_OPINION_VIRTUAL_ATTENTION,
		description: MODALITYS[EAppointmentModality.SECOND_OPINION_VIRTUAL_ATTENTION]
	}
]

export const DIARY_LABEL_COLORS: COLOR[] = [
	{
		id: 1,
		color: '#89ADFF'
	},
	{
		id: 2,
		color: '#D50000'
	},
	{
		id: 3,
		color: '#FFA29A'
	},
	{
		id: 4,
		color: '#F46B1E'
	},
	{
		id: 5,
		color: '#FFD55F'
	},
	{
		id: 6,
		color: '#009F4C'
	},
	{
		id: 7,
		color: '#A35AFF'
	},
	{
		id: 8,
		color: '#616161'
	},
	{
		id: 9,
		color: '#27BEFF'
	},
	{
		id: 10,
		color: '#08DDC3'
	}
];

export function getDiaryLabel(id: number): COLOR {
	return DIARY_LABEL_COLORS.find((diaryLabel: COLOR) => diaryLabel.id === id);
}

export interface COLOR {
	id: number,
	color: string,
}
export enum APPOINTMENT_CANCEL_OPTIONS {
	CURRENT_TURN = 1,
	CURRENT_AND_NEXTS_TURNS = 2,
	ALL_TURNS = 3
}

export enum RECURRING_APPOINTMENT_OPTIONS {
	NO_REPEAT = 1,
	EVERY_WEEK = 2,
	CUSTOM = 3
}

export const getAppointmentLabelColor = (appointmentStateId: number): string => {
	if (appointmentStateId === APPOINTMENT_STATES_ID.ASSIGNED)
		return Color.BLUE;

	if (appointmentStateId === APPOINTMENT_STATES_ID.ABSENT)
		return Color.RED;

	if (appointmentStateId === APPOINTMENT_STATES_ID.CONFIRMED)
		return Color.YELLOW;

	if (appointmentStateId === APPOINTMENT_STATES_ID.SERVED)
		return Color.GREEN;

	if (appointmentStateId === APPOINTMENT_STATES_ID.BOOKED)
		return Color.RED;
}
