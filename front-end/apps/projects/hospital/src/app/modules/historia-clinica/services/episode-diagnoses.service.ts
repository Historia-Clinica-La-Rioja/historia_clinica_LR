import { Injectable } from '@angular/core';
import { DiagnosisDto, HealthConditionDto } from '@api-rest/api-model';
import { deepClone } from '@core/utils/core.utils';

@Injectable({
	providedIn: 'root'
})
export class EpisodeDiagnosesService {

	episodeDiagnoses: EpisodeDiagnoses;

	constructor() { }

	setEpisodeDiagnoses(diagnoses: EpisodeDiagnoses) {
		this.episodeDiagnoses = diagnoses;
	}

	getEpisodeDiagnoses(): DiagnosisDto[] {
		const main = { ...this.episodeDiagnoses.main };
		const allEpisodeDiagnosis = deepClone(this.episodeDiagnoses.others);
		main && allEpisodeDiagnosis.push(main);
		return allEpisodeDiagnosis;
	}

}

interface EpisodeDiagnoses {
	main: HealthConditionDto,
	others: DiagnosisDto[],
}