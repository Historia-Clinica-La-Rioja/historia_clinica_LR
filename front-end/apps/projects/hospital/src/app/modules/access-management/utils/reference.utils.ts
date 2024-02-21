import { EReferenceAttentionState } from "@api-rest/api-model";
import { PRIORITY } from "@historia-clinica/modules/ambulatoria/constants/reference-masterdata";
import { Color, ColoredLabel } from "@presentation/colored-label/colored-label.component";
import { ColoredIconText } from "@presentation/components/colored-icon-text/colored-icon-text.component";
import { Priority } from "@presentation/components/priority/priority.component";
import { APPOINTMENT_STATES_ID } from "@turnos/constants/appointment";
import { PENDING, REFERENCE_STATES } from "@access-management/constants/reference";
export const REQUESTED_REFERENCE = null;

export function getPriority(id: number): string {
	if (id === PRIORITY.HIGH)
		return Priority.HIGH;
	if (id === PRIORITY.MEDIUM)
		return Priority.MEDIUM;
	return Priority.LOW;
}

export function getColoredIconText(closureType: string): ColoredIconText {
	return closureType ? { icon: "swap_horiz", text: closureType, color: Color.GREEN }
	 : { icon: "swap_horiz", text: "access-management.search_references.REQUESTED_REFERENCE", color: Color.YELLOW }
}

export function getAppointmentState(appointmentStateId: number): ColoredLabel {

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

	return PENDING;
}

export function getState(attentionState: EReferenceAttentionState): ColoredLabel {

	if (attentionState === EReferenceAttentionState.ASSIGNED)
		return {
			description: REFERENCE_STATES.ASSIGNED,
			color: Color.BLUE
		}

	if (attentionState === EReferenceAttentionState.SERVED)
		return {
			description: REFERENCE_STATES.SERVED,
			color: Color.GREEN
		}

	if (attentionState === EReferenceAttentionState.ABSENT)
		return {
			description: REFERENCE_STATES.ABSENT,
			color: Color.RED
		}

	return PENDING;
}
