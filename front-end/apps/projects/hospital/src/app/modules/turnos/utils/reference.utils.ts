import { EReferenceClosureType } from "@api-rest/api-model";
import { Color } from "@presentation/colored-label/colored-label.component";
import { ColoredIconText } from "@presentation/components/colored-icon-text/colored-icon-text.component";
import { Priority } from "@presentation/components/priority/priority.component";

export function getPriority(description: string): string {
	if (description === Priority.HIGH)
		return Priority.HIGH
	if (description === Priority.MEDIUM)
		return Priority.MEDIUM;
	return Priority.LOW;
}

export function getColoredIconText(closureType: EReferenceClosureType): ColoredIconText {
	if (closureType)
		return { icon: "swap_horiz", text: closureType.description, color: Color.YELLOW }
	return { icon: "swap_horiz", text: "turnos.search_references.REQUESTED_REFERENCE", color: Color.RED }
}