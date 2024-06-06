import { Color } from "@presentation/colored-label/colored-label.component"
import { State } from "../components/worklist/worklist.component"

export enum InformerStatus {
	COMPLETED = 1,
	DERIVED,
	PENDING
}

export function mapToState(statusId: number): State {
	if (statusId === InformerStatus.PENDING) {
		return { id: statusId, description: 'image-network.worklist.status.PENDING', color: Color.YELLOW }
	}

	if (statusId === InformerStatus.DERIVED) {
		return { id: statusId, description: 'image-network.worklist.status.DERIVED', color: Color.GREY }
	}

	return { id: statusId, description: 'image-network.worklist.status.COMPLETED', color: Color.GREEN }
}

export function getImageSizeInMB(sizeInBytes: number): String {
	return sizeInBytes ? (sizeInBytes / (1024 * 1024)).toFixed(2) : null
}


 export const toStudyLabel = (studies: string): string => studies.split(',').length > 1 ? `${ studies.split(',').at(0)} ...`
	: `${ studies.split(',').at(0)}`