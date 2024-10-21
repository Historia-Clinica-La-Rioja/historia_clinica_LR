import { Injectable } from '@angular/core';
import { ClinicalTermDto, DiagnosisDto, HealthConditionDto } from '@api-rest/api-model';

@Injectable({
	providedIn: 'root'
})
export class EpisodeDiagnosesService {

	episodeDiagnoses: EpisodeDiagnoses;

	constructor() { }

	setEpisodeDiagnoses(diagnoses: EpisodeDiagnoses) {
		this.episodeDiagnoses = diagnoses;
	}

	getEpisodeDiagnoses(): ClinicalTermDto[] {
		const main = <ClinicalTermDto>this.episodeDiagnoses.main;
		const allEpisodeDiagnosis = this.episodeDiagnoses.others.map(diagnosis => <ClinicalTermDto>diagnosis);
		main && allEpisodeDiagnosis.push(main);
		return allEpisodeDiagnosis;
	}

}

interface EpisodeDiagnoses {
	main: HealthConditionDto,
	others: DiagnosisDto[],
}