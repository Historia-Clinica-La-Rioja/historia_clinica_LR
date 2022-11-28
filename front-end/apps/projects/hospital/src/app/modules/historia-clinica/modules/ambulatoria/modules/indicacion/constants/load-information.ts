import { dateTimeDtotoLocalDate } from "@api-rest/mapper/date-dto.mapper";
import { EIndicationType } from "@api-rest/api-model"
import { OtherIndicationDto, ParenteralPlanDto } from "@api-rest/api-model"
import { OtherIndicationTypeDto } from "@api-rest/services/internment-indication.service"
import { ExtraInfo } from "@presentation/components/indication/indication.component"
import { OTHER_INDICATION_ID } from "./internment-indications"

export function showFrequency(indication: any): ExtraInfo[] {
	const hour = dateTimeDtotoLocalDate(indication?.startDateTime).getHours();
	if (indication?.frequency)
		return [{
			title: 'indicacion.internment-card.sections.indication-extra-description.START_TIME',
			content: "a las " + hour + "hs."
		}, {
			title: 'indicacion.internment-card.sections.indication-extra-description.INTERVAL',
			content: "cada " + indication?.frequency + "hs."
		}]
	if (indication?.event)
		return [{
			title: 'indicacion.internment-card.sections.indication-extra-description.EVENT',
			content: indication?.event
		}]
	if (indication?.startDateTime?.time.hours)
		return [{
			title: 'indicacion.internment-card.sections.indication-extra-description.ONCE',
			content: "a las " + hour + "hs."
		}]
	if (indication.periodUnit === 'e' && indication.type === EIndicationType.PHARMACO)
		return [{
			title: 'indicacion.internment-card.sections.indication-extra-description.EVENT',
			content: indication?.event
		}]
	if ((indication.periodUnit === 'h' || indication.periodUnit === 'd') && indication.type === EIndicationType.PHARMACO)
		return [{
			title: 'indicacion.internment-card.sections.indication-extra-description.ONCE',
			content: "a las " + hour + "hs."
		}]
	return [];
}

export function loadExtraInfoPharmaco(pharmaco: any, loadFrequency: boolean, vias?: any[]): ExtraInfo[] {
	const extra_info = [];
	if (pharmaco.dosage.quantity?.value) {
		extra_info.push({
			title: 'indicacion.internment-card.sections.indication-extra-description.DOSE',
			content: pharmaco.dosage.quantity.value + " " + pharmaco.dosage.quantity.unit + " "
		})
	}
	if (pharmaco.viaId)
		extra_info.push({
			title: 'indicacion.internment-card.sections.indication-extra-description.VIA',
			content: vias.find(v => v.id === pharmaco.viaId)?.description
		})
	else
		extra_info.push({
			title: 'indicacion.internment-card.sections.indication-extra-description.VIA',
			content: pharmaco.via
		})
	if (loadFrequency)
		return extra_info.concat(showFrequency(pharmaco.dosage));
	return extra_info;
}

export function loadExtraInfoParenteralPlan(parenteralPlan: ParenteralPlanDto, vias: any[]): ExtraInfo[] {
	const extra_info = [];
	if (parenteralPlan.dosage.quantity?.value) {
		extra_info.push({
			title: 'indicacion.internment-card.sections.indication-extra-description.VOL/BOLSA',
			content: parenteralPlan.dosage.quantity.value + " ml"
		})
	}
	if (parenteralPlan.via) {
		extra_info.push({
			title: 'indicacion.internment-card.sections.indication-extra-description.VIA',
			content: vias.find(v => v.id === parenteralPlan.via)?.description
		})
	}
	return extra_info;
}

export const getOtherIndicationType = (otherIndication: OtherIndicationDto, othersIndicatiosTypes: OtherIndicationTypeDto[]): string => {
	const result = othersIndicatiosTypes.find(i => i.id === otherIndication.otherIndicationTypeId);
	return (result.id === OTHER_INDICATION_ID) ? otherIndication.otherType : result.description;
}
