import { EElectronicSignatureStatus } from "@api-rest/api-model";
import { Color } from "@presentation/colored-label/colored-label.component"
import { ColoredIconText } from "@presentation/components/colored-icon-text/colored-icon-text.component"

export const INITIAL_PAGE = 0;
export const PAGE_SIZE = 5;
export const PAGE_SIZE_OPTIONS = [5, 10, 25];

export const SIGNATURE_STATUS_OUDATED: ColoredIconText = {
	text: 'firma-conjunta.STATE_SIGNATURE.OUTDATED',
	color: Color.GREY,
	icon: "cancel"
}

export const SIGNATURE_STATUS_PENDING: ColoredIconText = {
	text: 'firma-conjunta.STATE_SIGNATURE.PENDING',
	color: Color.YELLOW,
	icon: "timer"
}

export const SIGNATURE_STATUS_REJECTED: ColoredIconText = {
	text: 'firma-conjunta.STATE_SIGNATURE.REJECTED',
	color: Color.RED,
	icon: "cancel"
}

export const SIGNATURE_STATUS_SIGNED: ColoredIconText = {
	text: 'firma-conjunta.STATE_SIGNATURE.SIGNED',
		color: Color.GREEN,
		icon: "check_circle"
}

export const SIGNATURE_STATUS_OPTION = {
	[EElectronicSignatureStatus.OUTDATED]: SIGNATURE_STATUS_OUDATED,
	[EElectronicSignatureStatus.PENDING]: SIGNATURE_STATUS_PENDING,
	[EElectronicSignatureStatus.REJECTED]: SIGNATURE_STATUS_REJECTED,
	[EElectronicSignatureStatus.SIGNED]: SIGNATURE_STATUS_SIGNED
};
