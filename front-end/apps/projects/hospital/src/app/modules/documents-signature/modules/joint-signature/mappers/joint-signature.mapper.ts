import { EElectronicSignatureStatus, ElectronicSignatureInvolvedDocumentDto } from "@api-rest/api-model"
import { convertDateTimeDtoToDate } from "@api-rest/mapper/date-dto.mapper"
import { getDocumentTypeByEnum } from "@core/constants/summaries"
import { dateTimeToViewDateHourMinuteSecond, dateToViewDate } from "@core/utils/date.utils"
import { ColoredIconText } from "@presentation/components/colored-icon-text/colored-icon-text.component"
import { Detail } from "@presentation/components/details-section-custom/details-section-custom.component"
import { ItemListCard, ItemListOption } from "@presentation/components/selectable-card/selectable-card.component"
import { ShowMoreConceptsPipe } from "@presentation/pipes/show-more-concepts.pipe"
import { SIGNATURE_STATUS_OUDATED, SIGNATURE_STATUS_PENDING, SIGNATURE_STATUS_REJECTED, SIGNATURE_STATUS_SIGNED } from "../constants/joint-signature.constants"

export const buildItemListCard = (documents: ElectronicSignatureInvolvedDocumentDto[]): ItemListCard[] => {
	return documents.map(document => {
		return {
			id: document.documentId,
			icon: getDocumentTypeByEnum(document.documentType).icon,
			title: getDocumentTypeByEnum(document.documentType).title,
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
			value: [document.responsibleProfessionalCompleteName]
		},
		{
			title: 'digital-signature.card-information.PATIENT',
			value: [document.patientCompleteName],
		}
	]
}

const buildSignatureStatusOption = (status: EElectronicSignatureStatus): ItemListOption => {
	return {
		title: 'digital-signature.card-information.SIGN_STATUS',
		value: buildSignatureStatusValue(status)
	}
}

export const signatureStatusOptions = {
	[EElectronicSignatureStatus.OUTDATED]: SIGNATURE_STATUS_OUDATED,
	[EElectronicSignatureStatus.PENDING]: SIGNATURE_STATUS_PENDING,
	[EElectronicSignatureStatus.REJECTED]: SIGNATURE_STATUS_REJECTED,
	[EElectronicSignatureStatus.SIGNED]: SIGNATURE_STATUS_SIGNED
};

const buildSignatureStatusValue = (status: EElectronicSignatureStatus): ColoredIconText => {
	return signatureStatusOptions[status];
};

export const buildHeaderInformation = (document: ElectronicSignatureInvolvedDocumentDto): Detail[] => {
	return [
		{
			title: 'firma-conjunta.details.AMBIT',
			text: getDocumentTypeByEnum(document.documentType).title
		},
		{
			title: 'firma-conjunta.details.PATIENT',
			text: document.patientCompleteName
		},
		{
			title: 'firma-conjunta.details.DATE',
			text: dateToViewDate(convertDateTimeDtoToDate(document.documentCreationDate))
		},
		{
			title: 'firma-conjunta.details.PROFESSIONAL',
			text: document.responsibleProfessionalCompleteName
		},
	]
}
