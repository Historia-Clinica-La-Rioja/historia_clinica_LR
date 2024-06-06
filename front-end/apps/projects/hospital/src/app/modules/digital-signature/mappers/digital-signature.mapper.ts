import { DigitalSignatureDocumentDto } from "@api-rest/api-model"
import { getDocumentType } from "@core/constants/summaries"
import { dateTimeToViewDateHourMinuteSecond } from "@core/utils/date.utils"
import { ItemListCard, ItemListOption } from "@presentation/components/selectable-card/selectable-card.component"

export const buildItemListCard = (documents: DigitalSignatureDocumentDto[]): ItemListCard[] => {
	return documents.map(document => {
		return {
			id: document.documentId,
			icon: getDocumentType(document.documentTypeDto.id).icon,
			title: document.documentTypeDto.description,
			options: buildItemListOption(document)
		}
	})
}

export const buildItemListOption = (document: DigitalSignatureDocumentDto): ItemListOption[] => {
	return [
		{
			title: 'digital-signature.card-information.PROBLEM',
			value: document.snomedConcepts.length ? buildValues(document) : ['digital-signature.card-information.NO_SNOMED_CONCEPT']
		},
		{
			title: 'digital-signature.card-information.CREATED',
			value: [dateTimeToViewDateHourMinuteSecond(new Date(document.createdOn))],
		},
		{
			title: 'digital-signature.card-information.PROFESSIONAL',
			value: [document.professionalFullName]
		},
		{
			title: 'digital-signature.card-information.PATIENT',
			value: [document.patientFullName],
		},
		{
			title: 'digital-signature.card-information.ATTENTION_TYPE',
			value: [document.sourceTypeDto.description]
		},
	]
}

export const buildValues = (document: DigitalSignatureDocumentDto): string[] => {
	return document.snomedConcepts.map(sc => {
		if (sc.isMainHealthCondition)
			return ` ${sc.pt}` + ' (Principal)'
		return ` ${sc.pt}`
	})
}

export const buildTextOption = (title: string, content: string, okButtonLabel: string, cancelButtonLabel?: string): TextDialog => {
	return {
		title,
		content,
		cancelButtonLabel,
		okButtonLabel,
	}
}

export interface TextDialog {
	title: string,
	content: string,
	cancelButtonLabel?: string,
	okButtonLabel: string,
}
