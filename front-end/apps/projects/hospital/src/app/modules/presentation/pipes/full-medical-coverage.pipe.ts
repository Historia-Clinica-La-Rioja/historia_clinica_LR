import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
	name: 'fullMedicalCoverage'
})

export class FullMedicalCoveragePipe implements PipeTransform {
	transform(patientMedicalCoverage: any): string {
		const condition = (patientMedicalCoverage.condition) ? patientMedicalCoverage.condition.toLowerCase() : null;
		const medicalCoverageText = [patientMedicalCoverage.medicalCoverage.acronym, patientMedicalCoverage.medicalCoverage.name]
			.filter(Boolean).join(' - ');

		return [medicalCoverageText, patientMedicalCoverage.affiliateNumber, condition].filter(Boolean).join(' / ');
	}
}
