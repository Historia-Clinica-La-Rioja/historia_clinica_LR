import { Injectable } from '@angular/core';
import { DateFormat, dateISOParseDate, dateParse, newDate } from '@core/utils/moment.utils';
import { EMedicalCoverageType, HealthInsurance, PatientMedicalCoverage, PrivateHealthInsurance } from '@pacientes/dialogs/medical-coverage/medical-coverage.component';
import { BasicPatientDto, HealthInsuranceDto, PatientMedicalCoverageDto, PrivateHealthInsuranceDto } from '@api-rest/api-model';
import { toApiFormat } from '@api-rest/mapper/date.mapper';
import { PatientBasicData } from '@presentation/utils/patient.utils';

@Injectable({
	providedIn: 'root'
})
export class MapperService {

	toPatientMedicalCoverageDto: (s: PatientMedicalCoverage) => PatientMedicalCoverageDto = MapperService._toPatientMedicalCoverageDto;
	toPatientMedicalCoverage: (s: PatientMedicalCoverageDto) => PatientMedicalCoverage = MapperService._toPatientMedicalCoverage;
	toPatientBasicData: (o: BasicPatientDto) => PatientBasicData = MapperService._toPatientBasicData;
	constructor() { }


	private static _toPatientMedicalCoverageDto(s: PatientMedicalCoverage): PatientMedicalCoverageDto {
		return {
			affiliateNumber: s.affiliateNumber,
			medicalCoverage: s.medicalCoverage.toMedicalCoverageDto(),
			startDate: s.startDate ? toApiFormat(s.startDate) : null,
			endDate: s.endDate ? toApiFormat(s.endDate) : null,
			planId: s.planId,
			planName: s.planName,
			vigencyDate: s.validDate ? toApiFormat(s.validDate) : null,
			id: s.id,
			active: s.active,
			condition: s.condition
		};
	}

	private static _toPatientMedicalCoverage(s: PatientMedicalCoverageDto): PatientMedicalCoverage {

		return {
			id: s.id,
			affiliateNumber: s.affiliateNumber,
			validDate: s.vigencyDate ?
				dateParse(s.vigencyDate, DateFormat.API_DATE) : newDate(),
			medicalCoverage: toMedicalCoverage(s.medicalCoverage),
			startDate: s.startDate ? dateISOParseDate(s.startDate) : undefined,
			endDate: s.endDate ? dateISOParseDate(s.endDate) : undefined,
			planId: s.planId,
			planName: s.planName,
			active: s.active,
			condition: s.condition
		};

		// TODO ver la posibilidad de quitar ese if
		function toMedicalCoverage(dto: HealthInsuranceDto | PrivateHealthInsuranceDto ): HealthInsurance | PrivateHealthInsurance {
			return dto.type === EMedicalCoverageType.OBRASOCIAL ? new HealthInsurance(("rnos" in dto && dto.rnos) ? dto.rnos.toString() : null, ("acronym" in dto && dto.acronym) ? dto.acronym.toString() : null, dto.id, dto.name, dto.type)
				: new PrivateHealthInsurance(dto.id, dto.name, dto.type,dto.cuit);
		}

	}

	private static _toPatientBasicData<T extends BasicPatientDto>(patient: T): PatientBasicData {
		return {
			id: patient.id,
			firstName: patient.person?.firstName,
			middleNames: patient.person?.middleNames,
			lastName: patient.person?.lastName,
			otherLastNames: patient.person?.otherLastNames,
			gender: patient.person?.gender?.description,
			age: patient.person?.age,
			nameSelfDetermination: patient.person?.nameSelfDetermination,
			selfPerceivedGender: patient.person?.selfPerceivedGender,
			personAge: patient.person?.personAge
		};
	}


}
