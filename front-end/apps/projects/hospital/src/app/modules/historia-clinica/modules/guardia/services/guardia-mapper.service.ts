import { Injectable } from '@angular/core';
import { Triage } from '../components/triage-details/triage-details.component';
import { dateTimeDtoToDate, dateToDateDto, dateToTimeDto, dateToDateTimeDto, dateDtoToDate } from '@api-rest/mapper/date-dto.mapper';
import { TriageReduced } from '../routes/episode-details/episode-details.component';
import {
	AdministrativeDischargeDto,
	AMedicalDischargeDto,
	NewEffectiveClinicalObservationDto,
	NewEmergencyCareDto,
	NewVitalSignsObservationDto,
	PoliceInterventionDetailsDto,
	ResponseEmergencyCareDto,
	TriageListDto
} from '@api-rest/api-model';
import { parse } from 'date-fns';
import { Problema } from '../../../services/problemas-nueva-consulta.service';
import { DateFormat, dateToMoment, momentFormat } from '@core/utils/moment.utils';
import { MedicalDischargeForm } from '../routes/medical-discharge/medical-discharge.component';
import { EffectiveObservation, VitalSignsValue } from '../../../services/vital-signs-form.service';
import { AdministrativeForm } from '../routes/administrative-discharge/administrative-discharge.component';
import { AdministrativeAdmission } from './new-episode.service';

@Injectable({
	providedIn: 'root'
})

export class GuardiaMapperService {

	triageListDtoToTriage: (triageListDto: TriageListDto) => Triage = GuardiaMapperService._mapTriageListDtoToTriage;
	triageListDtoToTriageReduced: (triageListDto: TriageListDto) => TriageReduced = GuardiaMapperService._mapTriageListDtoToTriageReduced;
	vitalSignsValuetoNewVitalSignsObservationDto: (v: VitalSignsValue) => NewVitalSignsObservationDto = GuardiaMapperService._mapVitalSignsValuetoNewVitalSignsObservationDto;
	formToAMedicalDischargeDto: (s: MedicalDischargeForm) => AMedicalDischargeDto = GuardiaMapperService._mapFormToAMedicalDischargeDto;
	toAdministrativeDischargeDto: (s: AdministrativeForm) => AdministrativeDischargeDto = GuardiaMapperService._toAdministrativeDischargeDto;
	mapEffectiveObservationToNewEffectiveClinicalObservationDto: (e: EffectiveObservation) => NewEffectiveClinicalObservationDto = GuardiaMapperService._mapEffectiveObservationToNewEffectiveClinicalObservationDto;

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
			createdBy: triageListDto.createdBy,
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
			createdBy: triageListDto.createdBy,
			doctorsOfficeDescription: triageListDto.doctorsOffice?.description
		};
	}

	private static _mapFormToAMedicalDischargeDto(s: MedicalDischargeForm): AMedicalDischargeDto {
		return {
			medicalDischargeOn: {
				date: dateToDateDto(s.dateTime.date.toDate()),
				time: dateToTimeDto(parse(s.dateTime.time, 'HH:mm', new Date()))
			},
			problems: s.problems.map(
				(problema: Problema) => {
					return {
						chronic: problema.cronico,
						endDate: problema.fechaFin ? momentFormat(problema.fechaFin, DateFormat.API_DATE) : undefined,
						snomed: problema.snomed,
						startDate: momentFormat(problema.fechaInicio, DateFormat.API_DATE)
					};
				}
			),
			dischargeTypeId: s.dischargeTypeId,
			autopsy: s.autopsy,
		};

	}

	private static _toAdministrativeDischargeDto(s: AdministrativeForm): AdministrativeDischargeDto {
		return {
			administrativeDischargeOn: {
				date: dateToDateDto(s.dateTime.date.toDate()),
				time: dateToTimeDto(parse(s.dateTime.time, 'HH:mm', new Date()))
			},
			ambulanceCompanyId: s.ambulanceCompanyId,
			hospitalTransportId: s.hospitalTransportId,
		};
	}

	static _mapVitalSignsValuetoNewVitalSignsObservationDto(vitalSignsValue: VitalSignsValue): NewVitalSignsObservationDto {
		if (!vitalSignsValue) {
			return undefined;
		}
		const vitalSigns = {};
		Object.keys(vitalSignsValue).forEach((key: string) => {
			if (vitalSignsValue[key]) {
				vitalSigns[key] = {
					value: vitalSignsValue[key].value,
					effectiveTime: dateToDateTimeDto(vitalSignsValue[key].effectiveTime)
				};
			}
		});
		return vitalSigns !== {} ? vitalSigns : undefined;
	}

	static _mapEffectiveObservationToNewEffectiveClinicalObservationDto(effectiveObservation: EffectiveObservation): NewEffectiveClinicalObservationDto {
		if (!effectiveObservation) {
			return undefined;
		}

		return {
			value: effectiveObservation.value,
			effectiveTime: dateToDateTimeDto(effectiveObservation.effectiveTime)
		};
	}

	static _toAdministrativeAdmission(dto: ResponseEmergencyCareDto): AdministrativeAdmission {
		const callDate = dto.policeInterventionDetails?.callDate ? dateDtoToDate(dto.policeInterventionDetails.callDate) : null;
		const callTime = dto.policeInterventionDetails?.callTime ? dto.policeInterventionDetails?.callTime : null;
		return {
			ambulanceCompanyId: dto.ambulanceCompanyId ? dto.ambulanceCompanyId : null,
			callDate: callDate ? dateToMoment(callDate) : null,
			callTime: callTime ? callTime.hours + ':' + callTime.minutes : null,
			doctorsOfficeId: dto.doctorsOffice ? dto.doctorsOffice.id : null,
			emergencyCareEntranceTypeId: dto.entranceType?.id ? dto.entranceType.id : null,
			emergencyCareTypeId: dto.emergencyCareType?.id ? dto.emergencyCareType.id : null,
			firstName: dto.policeInterventionDetails?.firstName ? dto.policeInterventionDetails?.firstName : null,
			hasPoliceIntervention: dto.hasPoliceIntervention ? dto.hasPoliceIntervention : null,
			lastName: dto.policeInterventionDetails?.lastName ? dto.policeInterventionDetails?.lastName : null,
			patientId: dto.patient ? dto.patient.id : null,
			patientMedicalCoverageId: dto.patient?.patientMedicalCoverageId ? dto.patient.patientMedicalCoverageId : null,
			plateNumber: dto.policeInterventionDetails?.plateNumber ? dto.policeInterventionDetails.plateNumber : null,
			reasons: dto.reasons.map(s => ({ snomed: s })),
		};
	}

	static _toECAdministrativeDto(administrativeAdmission: AdministrativeAdmission): NewEmergencyCareDto {
		if (!administrativeAdmission) {
			return null;
		}
		const newEmergencyCareDto: NewEmergencyCareDto = {
			patient: {
				id: administrativeAdmission.patientId,
				patientMedicalCoverageId: administrativeAdmission.patientMedicalCoverageId
			},
			reasons: administrativeAdmission.reasons.map(s => s.snomed),
			emergencyCareTypeId: administrativeAdmission.emergencyCareTypeId,
			entranceTypeId: administrativeAdmission.emergencyCareEntranceTypeId,
			ambulanceCompanyId: administrativeAdmission.ambulanceCompanyId,
			doctorsOfficeId: administrativeAdmission.doctorsOfficeId,
			hasPoliceIntervention: administrativeAdmission.hasPoliceIntervention,
			policeInterventionDetails: toPoliceInterventionDetails()
		};
		return newEmergencyCareDto;

		function toPoliceInterventionDetails(): PoliceInterventionDetailsDto {
			return hasPoliceIntervention() ? {
				firstName: administrativeAdmission.firstName,
				lastName: administrativeAdmission.lastName,
				plateNumber: administrativeAdmission.plateNumber,
				callDate: administrativeAdmission.callDate ? dateToDateDto(administrativeAdmission.callDate.toDate()) : null,
				callTime: administrativeAdmission.callTime ? dateToTimeDto(parse(administrativeAdmission.callTime, 'HH:mm', new Date())) : null,
			} : null;
		}

		function hasPoliceIntervention(): boolean {
			if (administrativeAdmission?.callDate ||
				administrativeAdmission?.callTime ||
				administrativeAdmission?.plateNumber ||
				administrativeAdmission?.firstName ||
				administrativeAdmission?.lastName) {
				return true;
			}
			return false;
		}
	}
}
