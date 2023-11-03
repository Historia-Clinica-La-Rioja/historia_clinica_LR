import { EReferenceClosureType } from "@api-rest/api-model";
import { PRIORITY } from "@historia-clinica/modules/ambulatoria/constants/reference-masterdata";
import { Color } from "@presentation/colored-label/colored-label.component";
import { ColoredIconText } from "@presentation/components/colored-icon-text/colored-icon-text.component";
import { Priority } from "@presentation/components/priority/priority.component";

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