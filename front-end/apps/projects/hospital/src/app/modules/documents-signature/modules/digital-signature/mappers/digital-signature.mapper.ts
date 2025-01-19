import { DigitalSignatureDocumentDto } from "@api-rest/api-model"
import { SummaryItem } from "../../../components/summary-list-multiple-sign/summary-list-multiple-sign.component"
import { SummaryMultipleSignData } from "../../../components/summary-multiple-sign/summary-multiple-sign.component"
import { ShowMoreConceptsPipe } from "@presentation/pipes/show-more-concepts.pipe"
import { capitalize, capitalizeSentence } from "@core/utils/core.utils"
import { RegisterEditor } from "@presentation/components/register-editor-info/register-editor-info.component"

export const buildSummaryItemCard = (documents: DigitalSignatureDocumentDto[]): SummaryItem[] => {
	return documents.map(document => {
		return {
			id: document.documentId,
			data: buildSummaryMultipleSignData(document)
		}
	})
}

export const buildSummaryMultipleSignData = (document: DigitalSignatureDocumentDto): SummaryMultipleSignData => {
	const showMoreConceptsPipe = new ShowMoreConceptsPipe();
	return {
		title: document.documentTypeDto.description,
		patient: capitalizeSentence(document.patientFullName),
		problem: buildValues(document).length ? capitalize(showMoreConceptsPipe.transform(buildValues(document))) : 'digital-signature.card-information.NO_SNOMED_CONCEPT',
		registerEditor: buildRegisterEditor(document)
	}
}

export const buildRegisterEditor = (document: DigitalSignatureDocumentDto): RegisterEditor => {
	return {
		createdBy: capitalizeSentence(document.professionalFullName),
		date: new Date(document.createdOn)
	}
}

export const buildValues = (document: DigitalSignatureDocumentDto): string[] => {
	return document.snomedConcepts.map(sc => {
		return sc.pt
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
