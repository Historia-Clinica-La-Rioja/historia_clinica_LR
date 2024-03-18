import { Pipe, PipeTransform } from '@angular/core';
import { PatientGenderAgeDto } from '@api-rest/api-model';

const BINARY_GENDER_ID = 3;
const FEMALE_GENDER_ID = 1;
const PEDIATRIC_PATIENT = 19;

@Pipe({
	name: 'showTitleByPatientData'
})
export class ShowTitleByPatientDataPipe implements PipeTransform {

	transform(patientData: PatientGenderAgeDto): string {
		if (!patientData)
			return "";

		const age = patientData.age;
		const gender = patientData.gender;

		return (!age || !gender || age?.years > PEDIATRIC_PATIENT || gender?.id === BINARY_GENDER_ID)
			? "" : (gender.id === FEMALE_GENDER_ID) ? "| Niñas" : "| Niños";
	}

}
