import { Injectable } from '@angular/core';
import { CoverageDtoUnion, PatientMedicalCoverageDto } from '@api-rest/api-model';
import { HealthInsurance, PatientMedicalCoverage, PrivateHealthInsurance } from '@core/dialogs/medical-coverage/medical-coverage.component';
import { DateFormat, momentFormat, momentParse, momentParseDate, newMoment } from '@core/utils/moment.utils';

@Injectable({
	providedIn: 'root'
})
export class MapperService {

	toPatientMedicalCoverageDto: (s: PatientMedicalCoverage) => PatientMedicalCoverageDto = MapperService._toPatientMedicalCoverageDto;
	toPatientMedicalCoverage: (s: PatientMedicalCoverageDto) => PatientMedicalCoverage = MapperService._toPatientMedicalCoverage;

	constructor() { }


	private static _toPatientMedicalCoverageDto(s: PatientMedicalCoverage): PatientMedicalCoverageDto {
		let privateHealthInsuranceDetails;
		if (s.privateHealthInsuranceDetails?.startDate
			|| s.privateHealthInsuranceDetails?.endDate) {
			privateHealthInsuranceDetails = {
				startDate: s.privateHealthInsuranceDetails.startDate ?
					momentFormat(s.privateHealthInsuranceDetails.startDate, DateFormat.API_DATE) : null,
				endDate: s.privateHealthInsuranceDetails.endDate ?
					momentFormat(s.privateHealthInsuranceDetails.endDate, DateFormat.API_DATE) : null,
			};
		}
		return {
			affiliateNumber: s.affiliateNumber,
			medicalCoverage: s.medicalCoverage.toMedicalCoverageDto(),
			privateHealthInsuranceDetails,
			vigencyDate: momentFormat(s.validDate, DateFormat.API_DATE),
			id: s.id,
		}
	}

	private static _toPatientMedicalCoverage(s: PatientMedicalCoverageDto): PatientMedicalCoverage {
		let privateHealthInsuranceDetails;
		if (s.privateHealthInsuranceDetails.startDate
			|| s.privateHealthInsuranceDetails.endDate) {
			privateHealthInsuranceDetails = {
				startDate: momentParseDate(s.privateHealthInsuranceDetails.startDate),
				endDate: momentParseDate(s.privateHealthInsuranceDetails.endDate)
			};
		}
		// TODO ver la posibilidad de quitar ese if
		function toMedicalCoverage(dto: CoverageDtoUnion): HealthInsurance | PrivateHealthInsurance {
			return dto.type === 'HealthInsuranceDto' ? new HealthInsurance(dto.rnos.toString(), dto.acronym, dto.id, dto.name, dto.type)
				: new PrivateHealthInsurance(dto.plan, dto.id, dto.name, dto.type);
		}

		return {
			id: s.id,
			affiliateNumber: s.affiliateNumber,
			validDate: s.vigencyDate ?
				momentParse(s.vigencyDate, DateFormat.API_DATE) : newMoment(), // La fecha es nulleable pero nunca la guardamos asi
			medicalCoverage: toMedicalCoverage(s.medicalCoverage),
			privateHealthInsuranceDetails
		}
	}
}
