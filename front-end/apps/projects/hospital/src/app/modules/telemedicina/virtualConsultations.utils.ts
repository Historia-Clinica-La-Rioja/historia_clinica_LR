import { EVirtualConsultationPriority, EVirtualConsultationStatus } from "@api-rest/api-model"
import { Color } from "@presentation/colored-label/colored-label.component"
import { Priority } from "@presentation/components/priority/priority.component"

export const statusLabel = {
	[EVirtualConsultationStatus.FINISHED]: {
		description: 'Finalizada',
		color: Color.GREEN
	},
	[EVirtualConsultationStatus.IN_PROGRESS]: {
		description: 'En Progreso',
		color: Color.BLUE
	},
	[EVirtualConsultationStatus.PENDING]: {
		description: 'Pendiente',
		color: Color.YELLOW
	}
}

export const mapPriority = {
	[EVirtualConsultationPriority.HIGH]: Priority.HIGH,
	[EVirtualConsultationPriority.MEDIUM]: Priority.MEDIUM,
	[EVirtualConsultationPriority.LOW]: Priority.LOW,

}
