import { Pipe, PipeTransform } from '@angular/core';
import { StudyOrderBasicPatientDto } from '@api-rest/api-model';
import { convertDateDtoToDate } from '@api-rest/mapper/date-dto.mapper';
import { PatientSummary } from '@hsi-components/patient-summary/patient-summary.component';

@Pipe({
	name: 'patientSummary'
})
export class PatientSummaryPipe implements PipeTransform {
	transform(patient: StudyOrderBasicPatientDto): PatientSummary {
		return {
			fullName: this.getFullName(patient),
			identification: {
				type: 'DNI',
				number: patient.identificationNumber
			},
			id: patient.id,
			gender: this.getGender(patient.gender?.id),
			age: patient?.birthDate ? this.calculateAge(convertDateDtoToDate(patient.birthDate)) : null,
		};
	}

	private getFullName(patient: StudyOrderBasicPatientDto): string {
		const names = [
			patient.firstName,
			patient.middleNames,
			patient.lastName,
			patient.otherLastNames
		].filter(Boolean);
		return names.join(' ');
	}

	private getGender(genderId: number): string {
		switch (genderId) {
			case 1: return Gender.FEMALE;
			case 2: return Gender.MALE;
			default: return Gender.INDETERMINATE;
		}
	}

	private calculateAge(birthDate: Date): number {
		const today = new Date();
		const birthDateObj = birthDate;
		let age = today.getFullYear() - birthDateObj.getFullYear();
		const monthDiff = today.getMonth() - birthDateObj.getMonth();

		if (monthDiff < 0 || (monthDiff === 0 && today.getDate() < birthDateObj.getDate())) {
			age--;
		}

		return age;
	}
}

const Gender = {
	MALE: "Masculino",
	FEMALE: "Femenino",
	INDETERMINATE: "Indeterminado",
}
