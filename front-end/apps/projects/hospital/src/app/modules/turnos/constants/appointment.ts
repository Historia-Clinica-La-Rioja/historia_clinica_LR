import { EAppointmentModality } from "@api-rest/api-model";
import { Color, ColoredLabel } from "@presentation/colored-label/colored-label.component";

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
	BOOKED = 'Turno online',
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

export const onSiteAttentionColoredLabel : ColoredLabel = {
	description: MODALITYS[EAppointmentModality.ON_SITE_ATTENTION],
	color: Color.BLUE,
	icon: 'person',
}

export const virtualAttentionColoredLabel : ColoredLabel = {
	description: MODALITYS[EAppointmentModality.PATIENT_VIRTUAL_ATTENTION],
	color: Color.PURPLE,
	icon: 'video_call',
}