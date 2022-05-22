import { Injectable } from '@angular/core';
import { DateFormat, momentFormat, momentParse, momentParseDate, newMoment } from '@core/utils/moment.utils';
import { HealthInsurance, PatientMedicalCoverage, PrivateHealthInsurance } from '@presentation/dialogs/medical-coverage/medical-coverage.component';
import { CoverageDtoUnion, PatientMedicalCoverageDto } from '@api-rest/api-model';

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
			|| s.privateHealthInsuranceDetails?.endDate
			|| s.privateHealthInsuranceDetails?.planId) {
			privateHealthInsuranceDetails = {
				startDate: s.privateHealthInsuranceDetails.startDate ?
					momentFormat(s.privateHealthInsuranceDetails.startDate, DateFormat.API_DATE) : null,
				endDate: s.privateHealthInsuranceDetails.endDate ?
					momentFormat(s.privateHealthInsuranceDetails.endDate, DateFormat.API_DATE) : null,
				planId: s.privateHealthInsuranceDetails.planId,
				planName: s.privateHealthInsuranceDetails.planName
			};
		}
		return {
			affiliateNumber: s.affiliateNumber,
			medicalCoverage: s.medicalCoverage.toMedicalCoverageDto(),
			privateHealthInsuranceDetails,
			vigencyDate: momentFormat(s.validDate, DateFormat.API_DATE),
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
			privateHealthInsuranceDetails: mapDetails(),
			active: s.active,
			condition: s.condition
		};

		// TODO ver la posibilidad de quitar ese if
		function toMedicalCoverage(dto: CoverageDtoUnion): HealthInsurance | PrivateHealthInsurance {
			return dto.type === 'HealthInsuranceDto' ? new HealthInsurance((dto.rnos) ? dto.rnos.toString() : null, dto.acronym, dto.id, dto.name, dto.type)
				: new PrivateHealthInsurance(dto.id, dto.name, dto.type,dto.cuit);
		}

		function mapDetails() {
			let privateHealthInsuranceDetails;
			if (s.privateHealthInsuranceDetails) {
				privateHealthInsuranceDetails = {
					startDate: s.privateHealthInsuranceDetails.startDate ? momentParseDate(s.privateHealthInsuranceDetails.startDate) : undefined,
					endDate: s.privateHealthInsuranceDetails.endDate ? momentParseDate(s.privateHealthInsuranceDetails.endDate) : undefined,
					planId: s.privateHealthInsuranceDetails.planId,
					planName: s.privateHealthInsuranceDetails.planName
				};
			}
			return privateHealthInsuranceDetails;
		}
	}
}
