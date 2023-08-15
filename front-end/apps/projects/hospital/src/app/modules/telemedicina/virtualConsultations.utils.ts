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
	},
	[EVirtualConsultationStatus.CANCELED]: {
		description: 'Cancelada',
		color: Color.RED
	},
}

export const mapPriority = {
	[EVirtualConsultationPriority.HIGH]: Priority.HIGH,
	[EVirtualConsultationPriority.MEDIUM]: Priority.MEDIUM,
	[EVirtualConsultationPriority.LOW]: Priority.LOW,

}

export const status = {
	[EVirtualConsultationStatus.CANCELED]: EVirtualConsultationStatus.CANCELED,
	[EVirtualConsultationStatus.FINISHED]: EVirtualConsultationStatus.FINISHED,
	[EVirtualConsultationStatus.IN_PROGRESS]: EVirtualConsultationStatus.IN_PROGRESS,
	[EVirtualConsultationStatus.PENDING]: EVirtualConsultationStatus.PENDING
};
