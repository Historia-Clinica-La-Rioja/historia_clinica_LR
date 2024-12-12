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
			age: patient?.birthDate ? this.getAge(convertDateDtoToDate(patient.birthDate)) : null,
			monthsOfLife: patient?.birthDate ? this.calculateMonthsAndDaysOfLife(convertDateDtoToDate(patient.birthDate)) : null,
		}
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

	calculateMonthsAndDaysOfLife(birthDate: Date): string | null {
		const currentDate = new Date();

		let years = currentDate.getFullYear() - birthDate.getFullYear();
		let months = currentDate.getMonth() - birthDate.getMonth();
		let days = currentDate.getDate() - birthDate.getDate();

		if (days < 0) {
			months -= 1;
			const lastMonthDate = new Date(currentDate.getFullYear(), currentDate.getMonth(), 0);
			days += lastMonthDate.getDate();
		}

		if (months < 0) {
			years -= 1;
			months += 12;
		}

		if (years >= 1) {
			return null;
		}

		if (months === 0 && days > 0) {
			return days === 1 ? `${days} día` : `${days} días`;
		} else if (months > 0 && days === 0) {
			return months === 1 ? `${months} mes` : `${months} meses`;
		} else {
			const monthText = months === 1 ? `${months} mes` : `${months} meses`;
			const dayText = days === 1 ? `${days} día` : `${days} días`;
			return `${monthText} y ${dayText}`;
		}
	}

	private getAge(birthDate: Date): number {
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
