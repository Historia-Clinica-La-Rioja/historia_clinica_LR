import { Injectable } from '@angular/core';
import { NewEmergencyCareDto, DateTimeDto, PoliceInterventionDetailsDto } from '@api-rest/api-model';
import { dateToDateDto, dateToTimeDto } from '@api-rest/mapper/date-dto.mapper';
import { Moment } from 'moment';
import { MotivoConsulta } from '../../ambulatoria/services/motivo-nueva-consulta.service';

@Injectable()
export class NewEpisodeService {

	private administrativeAdmission: AdministrativeAdmission;

	constructor() { }

	destroy(): void {
		this.administrativeAdmission = undefined;
	}

	setAdministrativeAdmission(data: AdministrativeAdmission): void {
		this.administrativeAdmission = data;
	}

	getAdministrativeAdmissionDto(): NewEmergencyCareDto {

		if (!this.administrativeAdmission) {
			return null;
		}
		const policeInterventionDetails: PoliceInterventionDetailsDto = this.hasPoliceIntervention() ?
			this.toPoliceInterventionDetails() : null;
		const newEmergencyCareDto: NewEmergencyCareDto = {
			patient: {
				id: this.administrativeAdmission.patientId,
				patientMedicalCoverageId: this.administrativeAdmission.patientMedicalCoverageId
			},
			reasons: this.administrativeAdmission.reasons.map(s => s.snomed),
			emergencyCareTypeId: this.administrativeAdmission.emergencyCareTypeId,
			entranceTypeId: this.administrativeAdmission.emergencyCareEntranceTypeId,
			ambulanceCompanyId: this.administrativeAdmission.ambulanceCompanyId,
			doctorsOfficeId: this.administrativeAdmission.doctorsOfficeId,
			hasPoliceIntervention: this.administrativeAdmission.hasPoliceIntervention,
			policeInterventionDetails,
		};
		return newEmergencyCareDto;
	}

	getAdministrativeAdmission(): AdministrativeAdmission {
		return this.administrativeAdmission;
	}

	hasPoliceIntervention(): boolean {
		if (this.administrativeAdmission?.callDate ||
			this.administrativeAdmission?.callTime ||
			this.administrativeAdmission?.plateNumber ||
			this.administrativeAdmission?.firstName ||
			this.administrativeAdmission?.lastName) {
			return true;
		}
		return false;
	}


	private toPoliceInterventionDetails(): PoliceInterventionDetailsDto {

		return {
			callDate: this.administrativeAdmission.callDate ? dateToDateDto(this.administrativeAdmission.callDate.toDate()) : undefined,
			callTime: this.administrativeAdmission.callTime ? dateToTimeDto(getDateWithTime(getTimeArray(this.administrativeAdmission.callTime))) : undefined,
			plateNumber: this.administrativeAdmission.plateNumber,
			firstName: this.administrativeAdmission.firstName,
			lastName: this.administrativeAdmission.lastName
		};

		/**
		 * eg. 12:00
		 */
		function getTimeArray(timeString): string[] {
			return timeString.split(':');
		}

		function getDateWithTime(time: string[]): Date {
			const date = new Date();
			date.setHours(Number(time[0]), Number(time[1]));
			return date;
		}
	}

}

export interface AdministrativeAdmission {
	patientId: number;
	reasons: MotivoConsulta[];
	patientMedicalCoverageId: number;
	emergencyCareTypeId: number;
	doctorsOfficeId: number;
	emergencyCareEntranceTypeId: number;
	ambulanceCompanyId: string;
	hasPoliceIntervention: boolean;
	callDate: Moment;
	callTime: string;
	plateNumber: string;
	firstName: string;
	lastName: string;
}

export interface TriageAdultGynecologicalDto {
	categoryId: number;
	doctorsOfficeId: number;
	notes: string;
	vitalSigns?: {
		bloodOxygenSaturation?: {
			effectiveTime: DateTimeDto,
			value: number,
		},
		diastolicBloodPressure?: {
			effectiveTime: DateTimeDto,
			value: number,
		},
		heartRate?: {
			effectiveTime: DateTimeDto,
			value: number,
		},
		respiratoryRate?: {
			effectiveTime: DateTimeDto,
			value: number,
		},
		systolicBloodPressure?: {
			effectiveTime: DateTimeDto,
			value: number,
		},
		temperature?: {
			effectiveTime: DateTimeDto,
			value: number,
		},
	};
}
