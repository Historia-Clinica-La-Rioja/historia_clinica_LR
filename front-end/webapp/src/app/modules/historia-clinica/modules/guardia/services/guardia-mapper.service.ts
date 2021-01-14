import { Injectable } from '@angular/core';
import { Triage } from '../components/triage-details/triage-details.component';
import { EmergencyCareTypes } from '../constants/masterdata';
import { dateTimeDtoToDate } from '@api-rest/mapper/date-dto.mapper';

@Injectable({
	providedIn: 'root'
})
export class GuardiaMapperService {

	triageDtoToTriage: (triageDto: any) => Triage = GuardiaMapperService._mapTriageDtoToTriage;

	constructor() {
	}

	private static _mapTriageDtoToTriage(triageDto: any): Triage {
		return {
			emergencyCareType: EmergencyCareTypes.PEDIATRIA,
			creationDate: dateTimeDtoToDate(triageDto.creationDate),
			category: {
				id: triageDto.category.id,
				name: triageDto.category.name,
				colorHex: triageDto.category.color.code
			},
			professional: triageDto.professional,
			doctorsOfficeDescription: triageDto.doctorsOffice?.description,
			vitalSigns: mapVitalSigns(triageDto.vitalSigns),
			appearance: mapAppearance(triageDto.appearance),
			breathing: mapBreathing(triageDto.breathing),
			circulation: mapCirculation(triageDto.circulation),
			notes: triageDto.notes
		};

		function mapAppearance(appearance) {
			if (!appearance) {
				return undefined;
			}
			return {
				bodyTemperatureDescription: appearance.bodyTemperature?.description,
				cryingExcessive: appearance.cryingExcessive,
				muscleHypertoniaDescription: appearance.muscleHypertonia?.description
			};
		}

		function mapBreathing(breathing) {
			if (!breathing) {
				return undefined;
			}
			return {
				respiratoryRetractionDescription: breathing.respiratoryRetraction?.description,
				stridor: breathing.stridor,
				respiratoryRate: breathing.respiratoryRate ? {
					value: breathing.respiratoryRate.value,
					effectiveTime: dateTimeDtoToDate(breathing.respiratoryRate.effectiveTime)
				} : undefined,
				bloodOxygenSaturation: breathing.bloodOxygenSaturation ? {
					value: breathing.bloodOxygenSaturation.value,
					effectiveTime: dateTimeDtoToDate(breathing.bloodOxygenSaturation.effectiveTime)
				} : undefined
			};
		}

		function mapCirculation(circulation) {
			if (!circulation) {
				return undefined;
			}
			return {
				perfusionDescription: circulation.perfusion?.description,
				heartRate: circulation.heartRate ? {
					value: circulation.heartRate.value,
					effectiveTime: dateTimeDtoToDate(circulation.heartRate.effectiveTime),
				} : undefined,
			};
		}

		function mapVitalSigns(vitalSigns) {
			if (!vitalSigns) {
				return undefined;
			}
			return {
				bloodOxygenSaturation: vitalSigns.bloodOxygenSaturation ? {
					value: vitalSigns.bloodOxygenSaturation.value,
					effectiveTime: dateTimeDtoToDate(vitalSigns.bloodOxygenSaturation.effectiveTime)
				} : undefined,
				diastolicBloodPressure: vitalSigns.diastolicBloodPressure ? {
					value: vitalSigns.diastolicBloodPressure.value,
					effectiveTime: dateTimeDtoToDate(vitalSigns.diastolicBloodPressure.effectiveTime)
				} : undefined,
				heartRate: vitalSigns.heartRate ? {
					value: vitalSigns.heartRate.value,
					effectiveTime: dateTimeDtoToDate(vitalSigns.heartRate.effectiveTime)
				} : undefined,
				respiratoryRate: vitalSigns.respiratoryRate ? {
					value: vitalSigns.respiratoryRate.value,
					effectiveTime: dateTimeDtoToDate(vitalSigns.respiratoryRate.effectiveTime)
				} : undefined,
				systolicBloodPressure: vitalSigns.systolicBloodPressure ? {
					value: vitalSigns.systolicBloodPressure.value,
					effectiveTime: dateTimeDtoToDate(vitalSigns.systolicBloodPressure.effectiveTime)
				} : undefined,
				temperature: vitalSigns.temperature ? {
					value: vitalSigns.temperature.value,
					effectiveTime: dateTimeDtoToDate(vitalSigns.temperature.effectiveTime)
				} : undefined
			};
		}

	}
}
