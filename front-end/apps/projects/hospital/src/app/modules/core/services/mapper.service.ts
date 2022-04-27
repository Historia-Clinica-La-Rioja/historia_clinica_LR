import { Injectable } from '@angular/core';
import { DateFormat, momentFormat, momentParse, momentParseDate, newMoment } from '@core/utils/moment.utils';
import { EMedicalCoverageType, HealthInsurance, PatientMedicalCoverage, PrivateHealthInsurance } from '@pacientes/dialogs/medical-coverage/medical-coverage.component';
import { HealthInsuranceDto, PatientMedicalCoverageDto, PrivateHealthInsuranceDto } from '@api-rest/api-model';

@Injectable({
	providedIn: 'root'
})
export class MapperService {

	toPatientMedicalCoverageDto: (s: PatientMedicalCoverage) => PatientMedicalCoverageDto = MapperService._toPatientMedicalCoverageDto;
	toPatientMedicalCoverage: (s: PatientMedicalCoverageDto) => PatientMedicalCoverage = MapperService._toPatientMedicalCoverage;

	constructor() { }


	private static _toPatientMedicalCoverageDto(s: PatientMedicalCoverage): PatientMedicalCoverageDto {
		return {
			affiliateNumber: s.affiliateNumber,
			medicalCoverage: s.medicalCoverage.toMedicalCoverageDto(),
			startDate: s.startDate ? momentFormat(s.startDate, DateFormat.API_DATE) : null,
			endDate: s.endDate ? momentFormat(s.endDate, DateFormat.API_DATE) : null,
			planId: s.planId,
			planName: s.planName,
			vigencyDate: s.validDate ? momentFormat(s.validDate, DateFormat.API_DATE) : null,
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
				momentParse(s.vigencyDate, DateFormat.API_DATE) : newMoment(),
			medicalCoverage: toMedicalCoverage(s.medicalCoverage),
			startDate: s.startDate ? momentParseDate(s.startDate) : undefined,
			endDate: s.endDate ? momentParseDate(s.endDate) : undefined,
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
}
