import { EElectronicSignatureStatus } from "@api-rest/api-model";
import { Color } from "@presentation/colored-label/colored-label.component"
import { ColoredIconText } from "@presentation/components/colored-icon-text/colored-icon-text.component"
import { Filter } from "@presentation/components/filters/filters.component";

export const INITIAL_PAGE = 0;
export const PAGE_SIZE = 5;
export const PAGE_SIZE_OPTIONS = [5, 10, 25];
export const SIGNATURE_STATUS_KEY = 'signature-status'

export const SIGNATURE_STATUS_OUTDATED: ColoredIconText = {
	text: 'firma-conjunta.state-signature.OUTDATED',
	color: Color.GREY,
	icon: "cancel"
}

export const SIGNATURE_STATUS_PENDING: ColoredIconText = {
	text: 'firma-conjunta.state-signature.PENDING',
	color: Color.YELLOW,
	icon: "timer"
}

export const SIGNATURE_STATUS_REJECTED: ColoredIconText = {
	text: 'firma-conjunta.state-signature.REJECTED',
	color: Color.RED,
	icon: "cancel"
}

export const SIGNATURE_STATUS_SIGNED: ColoredIconText = {
	text: 'firma-conjunta.state-signature.SIGNED',
	color: Color.GREEN,
	icon: "check_circle"
}

export const SIGNATURE_STATUS_OPTION = {
	[EElectronicSignatureStatus.OUTDATED]: SIGNATURE_STATUS_OUTDATED,
	[EElectronicSignatureStatus.PENDING]: SIGNATURE_STATUS_PENDING,
	[EElectronicSignatureStatus.REJECTED]: SIGNATURE_STATUS_REJECTED,
	[EElectronicSignatureStatus.SIGNED]: SIGNATURE_STATUS_SIGNED
};

export const SIGNATURE_STATUS_FILTER: Filter[] = [{
	key: SIGNATURE_STATUS_KEY,
	name: 'firma-conjunta.signature-status-filter.LABEL',
	isMultiple:true,
	options: [{
		id: EElectronicSignatureStatus.PENDING,
		description: 'firma-conjunta.signature-status-filter.PENDING',
	},
	{
		id: EElectronicSignatureStatus.REJECTED,
		description: 'firma-conjunta.signature-status-filter.REJECTED',
	},
	{
		id: EElectronicSignatureStatus.SIGNED,
		description: 'firma-conjunta.signature-status-filter.SIGNED',
	},
	{
		id: EElectronicSignatureStatus.OUTDATED,
		description: 'firma-conjunta.signature-status-filter.OUTDATED',
	},]
}]
