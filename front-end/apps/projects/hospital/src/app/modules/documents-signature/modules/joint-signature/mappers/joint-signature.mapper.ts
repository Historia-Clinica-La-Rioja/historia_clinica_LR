import { EElectronicSignatureStatus, ElectronicSignatureInvolvedDocumentDto } from "@api-rest/api-model"
import { convertDateTimeDtoToDate } from "@api-rest/mapper/date-dto.mapper"
import { getDocumentType } from "@core/constants/summaries"
import { dateTimeToViewDateHourMinuteSecond, dateToViewDate } from "@core/utils/date.utils"
import { ColoredIconText } from "@presentation/components/colored-icon-text/colored-icon-text.component"
import { Detail } from "@presentation/components/details-section-custom/details-section-custom.component"
import { ItemListCard, ItemListOption } from "@presentation/components/selectable-card/selectable-card.component"
import { ShowMoreConceptsPipe } from "@presentation/pipes/show-more-concepts.pipe"
import { SIGNATURE_STATUS_OPTION } from "../constants/joint-signature.constants"
import { capitalizeSentence } from "@core/utils/core.utils"

export const buildItemListCard = (documents: ElectronicSignatureInvolvedDocumentDto[]): ItemListCard[] => {
	return documents.map(document => {
		return {
			id: document.documentId,
			icon: getDocumentType(document.documentTypeId).icon,
			title: getDocumentType(document.documentTypeId).title,
			options: buildItemListOption(document),
			coloredIconTextOption: buildSignatureStatusOption(document.signatureStatus)
		}
	})
}

const buildItemListOption = (document: ElectronicSignatureInvolvedDocumentDto): ItemListOption[] => {
	const showMoreConceptsPipe = new ShowMoreConceptsPipe();
	return [
		{
			title: 'digital-signature.card-information.PROBLEM',
			value: document.problems.length ? [showMoreConceptsPipe.transform(document.problems)] : ['digital-signature.card-information.NO_SNOMED_CONCEPT']
		},
		{
			title: 'digital-signature.card-information.CREATED',
			value: [dateTimeToViewDateHourMinuteSecond(convertDateTimeDtoToDate(document.documentCreationDate))],
		},
		{
			title: 'digital-signature.card-information.PROFESSIONAL',
			value: [capitalizeSentence(document.responsibleProfessionalCompleteName)]
		},
		{
			title: 'digital-signature.card-information.PATIENT',
			value: [capitalizeSentence(document.patientCompleteName)],
		}
	]
}

const buildSignatureStatusOption = (status: EElectronicSignatureStatus): ItemListOption => {
	return {
		title: 'digital-signature.card-information.SIGN_STATUS',
		value: buildSignatureStatusValue(status)
	}
}

const buildSignatureStatusValue = (status: EElectronicSignatureStatus): ColoredIconText => {
	return SIGNATURE_STATUS_OPTION[status];
};

export const buildHeaderInformation = (document: ElectronicSignatureInvolvedDocumentDto): Detail[] => {
	return [
		{
			title: 'firma-conjunta.details.AMBIT',
			text: getDocumentType(document.documentTypeId).title
		},
		{
			title: 'firma-conjunta.details.PATIENT',
			text: capitalizeSentence(document.patientCompleteName)
		},
		{
			title: 'firma-conjunta.details.DATE',
			text: dateToViewDate(convertDateTimeDtoToDate(document.documentCreationDate))
		},
		{
			title: 'firma-conjunta.details.PROFESSIONAL',
			text: capitalizeSentence(document.responsibleProfessionalCompleteName)
		},
	]
}
