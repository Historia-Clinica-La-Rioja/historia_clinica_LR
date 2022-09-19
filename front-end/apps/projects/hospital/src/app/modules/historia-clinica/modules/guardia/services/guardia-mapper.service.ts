import { Injectable } from '@angular/core';
import { Triage } from '../components/triage-details/triage-details.component';
import { dateTimeDtoToDate, dateToDateDto, dateToTimeDto, dateToDateTimeDto, dateDtoToDate, timeDtoToDate } from '@api-rest/mapper/date-dto.mapper';
import { TriageReduced } from '../routes/episode-details/episode-details.component';
import {
	AdministrativeDischargeDto,
	AMedicalDischargeDto,
	NewEffectiveClinicalObservationDto,
	NewEmergencyCareDto,
	NewRiskFactorsObservationDto,
	PoliceInterventionDetailsDto,
	ResponseEmergencyCareDto,
	TriageListDto
} from '@api-rest/api-model';
import { parse } from 'date-fns';
import { Problema } from '../../../services/problemas.service';
import { DateFormat, dateToMoment, momentFormat } from '@core/utils/moment.utils';
import { MedicalDischargeForm } from '../routes/medical-discharge/medical-discharge.component';
import { AdministrativeForm } from '../routes/administrative-discharge/administrative-discharge.component';
import { AdministrativeAdmission } from './new-episode.service';
import { EffectiveObservation, RiskFactorsValue } from '@historia-clinica/services/factores-de-riesgo-form.service';

@Injectable({
	providedIn: 'root'
})

export class GuardiaMapperService {

	triageListDtoToTriage: (triageListDto: TriageListDto) => Triage = GuardiaMapperService._mapTriageListDtoToTriage;
	triageListDtoToTriageReduced: (triageListDto: TriageListDto) => TriageReduced = GuardiaMapperService._mapTriageListDtoToTriageReduced;
	riskFactorsValuetoNewRiskFactorsObservationDto: (v: RiskFactorsValue) => NewRiskFactorsObservationDto = GuardiaMapperService._mapRiskFactorsValuetoNewRiskFactorsObservationDto;
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
			riskFactors: mapRiskFactors(triageListDto.riskFactors),
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

		function mapRiskFactors(riskFactors: NewRiskFactorsObservationDto) {
			if (!riskFactors) {
				return undefined;
			}
			return {
				bloodOxygenSaturation: riskFactors.bloodOxygenSaturation ? {
					value: riskFactors.bloodOxygenSaturation.value,
					effectiveTime: dateTimeDtoToDate(riskFactors.bloodOxygenSaturation.effectiveTime)
				} : undefined,
				diastolicBloodPressure: riskFactors.diastolicBloodPressure ? {
					value: riskFactors.diastolicBloodPressure.value,
					effectiveTime: dateTimeDtoToDate(riskFactors.diastolicBloodPressure.effectiveTime)
				} : undefined,
				heartRate: riskFactors.heartRate ? {
					value: riskFactors.heartRate.value,
					effectiveTime: dateTimeDtoToDate(riskFactors.heartRate.effectiveTime)
				} : undefined,
				respiratoryRate: riskFactors.respiratoryRate ? {
					value: riskFactors.respiratoryRate.value,
					effectiveTime: dateTimeDtoToDate(riskFactors.respiratoryRate.effectiveTime)
				} : undefined,
				systolicBloodPressure: riskFactors.systolicBloodPressure ? {
					value: riskFactors.systolicBloodPressure.value,
					effectiveTime: dateTimeDtoToDate(riskFactors.systolicBloodPressure.effectiveTime)
				} : undefined,
				temperature: riskFactors.temperature ? {
					value: riskFactors.temperature.value,
					effectiveTime: dateTimeDtoToDate(riskFactors.temperature.effectiveTime)
				} : undefined,
				bloodGlucose: riskFactors.bloodGlucose ? {
					value: riskFactors.bloodGlucose.value,
					effectiveTime: dateTimeDtoToDate(riskFactors.bloodGlucose.effectiveTime)
				} : undefined,
				glycosylatedHemoglobin: riskFactors.glycosylatedHemoglobin ? {
					value: riskFactors.glycosylatedHemoglobin.value,
					effectiveTime: dateTimeDtoToDate(riskFactors.glycosylatedHemoglobin.effectiveTime)
				} : undefined,
				cardiovascularRisk: riskFactors.cardiovascularRisk ? {
					value: riskFactors.cardiovascularRisk.value,
					effectiveTime: dateTimeDtoToDate(riskFactors.cardiovascularRisk.effectiveTime)
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
						severity: problema.codigoSeveridad,
						chronic: problema.cronico,
						endDate: problema.fechaFin ? momentFormat(problema.fechaFin, DateFormat.API_DATE) : undefined,
						snomed: problema.snomed,
						startDate: problema.fechaInicio ? momentFormat(problema.fechaInicio, DateFormat.API_DATE) : undefined
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

	static _mapRiskFactorsValuetoNewRiskFactorsObservationDto(riskFactorsValue: RiskFactorsValue): NewRiskFactorsObservationDto {
		if (!riskFactorsValue) {
			return undefined;
		}
		const riskFactors = {};
		Object.keys(riskFactorsValue).forEach((key: string) => {
			if (riskFactorsValue[key]) {
				riskFactors[key] = {
					value: riskFactorsValue[key].value,
					effectiveTime: dateToDateTimeDto(riskFactorsValue[key].effectiveTime)
				};
			}
		});
		return riskFactors !== {} ? riskFactors : undefined;
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
		const callTime = dto.policeInterventionDetails?.callTime ? timeDtoToDate(dto.policeInterventionDetails.callTime) : null;
		return {
			ambulanceCompanyId: dto.ambulanceCompanyId ? dto.ambulanceCompanyId : null,
			callDate: callDate ? dateToMoment(callDate) : null,
			callTime: callTime ? momentFormat(dateToMoment(callTime), DateFormat.HOUR_MINUTE) : null,
			doctorsOfficeId: dto.doctorsOffice ? dto.doctorsOffice.id : null,
			emergencyCareEntranceTypeId: dto.entranceType?.id ? dto.entranceType.id : null,
			emergencyCareTypeId: dto.emergencyCareType?.id ? dto.emergencyCareType.id : null,
			firstName: dto.policeInterventionDetails?.firstName ? dto.policeInterventionDetails?.firstName : null,
			hasPoliceIntervention: dto.hasPoliceIntervention === true || dto.hasPoliceIntervention === false ? dto.hasPoliceIntervention : null,
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
