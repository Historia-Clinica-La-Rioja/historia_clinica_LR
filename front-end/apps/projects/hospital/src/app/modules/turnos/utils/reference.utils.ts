import { EReferenceClosureType } from "@api-rest/api-model";
import { PRIORITY } from "@historia-clinica/modules/ambulatoria/constants/reference-masterdata";
import { Color } from "@presentation/colored-label/colored-label.component";
import { ColoredIconText } from "@presentation/components/colored-icon-text/colored-icon-text.component";
import { DescriptionPriority } from "@presentation/components/priority-select/priority-select.component";
import { Priority } from "@presentation/components/priority/priority.component";
import { ReferenceState } from "@turnos/components/report-information/report-information.component";
import { APPOINTMENT_STATES_ID } from "@turnos/constants/appointment";

export function getPriority(id: number): string {
	if (id === PRIORITY.HIGH)
		return Priority.HIGH;
	if (id === PRIORITY.MEDIUM)
		return Priority.MEDIUM;
	return Priority.LOW;
}

export function getColoredIconText(closureType: EReferenceClosureType): ColoredIconText {
	if (closureType)
		return { icon: "swap_horiz", text: closureType.description, color: Color.YELLOW }
	return { icon: "swap_horiz", text: "turnos.search_references.REQUESTED_REFERENCE", color: Color.RED }
}

export enum REFERENCE_STATES {
	PENDING = 'PENDIENTE',
	ASSIGNED = 'ASIGNADO',
	ABSENT = 'AUSENTE',
	SERVED = 'ATENDIDO',
}



export function getReferenceState(appointmentStateId: number): ReferenceState {

	if (appointmentStateId === APPOINTMENT_STATES_ID.ASSIGNED || appointmentStateId === APPOINTMENT_STATES_ID.CONFIRMED)
		return {
			description: REFERENCE_STATES.ASSIGNED,
			color: Color.BLUE
		}

	if (appointmentStateId === APPOINTMENT_STATES_ID.SERVED)
		return {
			description: REFERENCE_STATES.SERVED,
			color: Color.GREEN
		}

	if (appointmentStateId === APPOINTMENT_STATES_ID.ABSENT)
		return {
			description: REFERENCE_STATES.ABSENT,
			color: Color.RED
		}

	return {
		description: REFERENCE_STATES.PENDING,
		color: Color.YELLOW
	}
}

export const APPOINTMENT_STATE = [{
	id: APPOINTMENT_STATES_ID.ASSIGNED,
	description: REFERENCE_STATES.ASSIGNED
}, {
	id: APPOINTMENT_STATES_ID.ABSENT,
	description: REFERENCE_STATES.ABSENT
}, {
	id: APPOINTMENT_STATES_ID.SERVED,
	description: REFERENCE_STATES.SERVED
}, {
	id: -1,
	description: REFERENCE_STATES.PENDING
}]


export const PRIORITY_OPTIONS = [{
	id: 1,
	description: DescriptionPriority.HIGH
}, {
	id: 2,
	description: DescriptionPriority.MEIDUM
}, {
	id: 3,
	description: DescriptionPriority.LOW
}]

export const CLOSURE_OPTIONS = [
	{
		id: -1,
		description: "Referencia solicitada"
	}, {
		id: 1,
		description: "Continúa en observación"
	}, {
		id: 2,
		description: "Inicia tratamiento en centro de referencia"
	}, {
		id: 3,
		description: "Requiere estudios complementarios"
	}, {
		id: 4,
		description: "Contrarreferencia"
	}
];
