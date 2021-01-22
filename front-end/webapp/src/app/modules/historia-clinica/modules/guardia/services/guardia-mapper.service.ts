import { Injectable } from '@angular/core';
import { Triage } from '../components/triage-details/triage-details.component';
import { dateTimeDtoToDate } from '@api-rest/mapper/date-dto.mapper';
import { TriageReduced } from '../routes/episode-details/episode-details.component';
import { NewVitalSignsObservationDto, TriageListDto } from '@api-rest/api-model';

@Injectable({
	providedIn: 'root'
})

export class GuardiaMapperService {

	triageListDtoToTriage: (triageListDto: TriageListDto) => Triage = GuardiaMapperService._mapTriageListDtoToTriage;
	triageListDtoToTriageReduced: (triageListDto: TriageListDto) => TriageReduced = GuardiaMapperService._mapTriageListDtoToTriageReduced;

	constructor() {
	}

	private static _mapTriageListDtoToTriage(triageListDto: TriageListDto): Triage {
		return {
			creationDate: dateTimeDtoToDate(triageListDto.creationDate),
			category: {
				id: triageListDto.category.id,
				name: triageListDto.category.name,
				colorHex: triageListDto.category.color.code
			},
			professional: triageListDto.professional,
			doctorsOfficeDescription: triageListDto.doctorsOffice?.description,
			vitalSigns: mapVitalSigns(triageListDto.vitalSigns),
			appearance: mapAppearance(triageListDto.appearance),
			breathing: mapBreathing(triageListDto.breathing),
			circulation: mapCirculation(triageListDto.circulation),
			notes: triageListDto.notes
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

		function mapVitalSigns(vitalSigns: NewVitalSignsObservationDto) {
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

	private static _mapTriageListDtoToTriageReduced(triageListDto: TriageListDto): TriageReduced {
		return {
			creationDate: dateTimeDtoToDate(triageListDto.creationDate),
			category: {
				id: triageListDto.category.id,
				name: triageListDto.category.name,
				colorHex: triageListDto.category.color.code
			},
			professional: triageListDto.professional,
			doctorsOfficeDescription: triageListDto.doctorsOffice?.description
		};
	}
}
