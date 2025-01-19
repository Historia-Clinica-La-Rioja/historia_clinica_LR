import { WCParams } from '@extensions/components/ui-external-component/ui-external-component.component';
import { SlotedInfo } from "./wc-extensions.service";
import { MenuItem } from '@presentation/components/menu/menu.component';

export const findSlotedInfoById = (wcId: string) =>
	(slotedInfoList: SlotedInfo[] = []): SlotedInfo =>
		slotedInfoList.find(e => wcId === e.componentName);

export const toInstitutionWCParams = (institutionId: number) =>
	(element: SlotedInfo): WCParams =>
		slotedInfoToWCParamsMapper(element, { institutionId });

export const slotedInfoToWCParamsMapper = (element: SlotedInfo, params): WCParams => element ? {
	title: element.title,
	componentName: element.componentName,
	url: element.fullUrl,
	params,
} : undefined;

export const toClinicHistoryWCParams = (patientId: number) =>
	(element: SlotedInfo): WCParams =>
		slotedInfoToWCParamsMapper(element, { patientId });

export const toPatientProfileWCParams = (patientId: number) =>
	(element: SlotedInfo): WCParams =>
		slotedInfoToWCParamsMapper(element, { patientId });

export const toSystemHomeWCParams = (element: SlotedInfo): WCParams =>
	slotedInfoToWCParamsMapper(element, {});

export const toInstitutionHomeWCParams = (element: SlotedInfo, institutionId: number): WCParams =>
	slotedInfoToWCParamsMapper(element, { institutionId });

const listMap = (mapper: (from: SlotedInfo) => WCParams) =>
	(elements: SlotedInfo[]): WCParams[] => elements.map(mapper);

export const toInstitutionWCParamsList = (institutionId: number) =>
	listMap(toInstitutionWCParams(institutionId));

export const toSystemHomeWCParamsList = listMap(toSystemHomeWCParams);

const toMenuItem = (slotedInfo: SlotedInfo): MenuItem => ({
	label: {
		text: slotedInfo.title,
	},
	icon: 'home',
	id: slotedInfo.title.split(' ').join('-'),
	url: `web-components/${slotedInfo.componentName}`,
});

export const toMenuItemList = (elements: SlotedInfo[]): MenuItem[] => elements.map(toMenuItem);
