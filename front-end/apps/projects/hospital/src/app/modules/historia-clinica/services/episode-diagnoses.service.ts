import { Injectable } from '@angular/core';
import { DiagnosisDto, HealthConditionDto } from '@api-rest/api-model';
import { deepClone } from '@core/utils/core.utils';
import { BehaviorSubject } from 'rxjs';

@Injectable({
	providedIn: 'root'
})
export class EpisodeDiagnosesService {

	episodeDiagnoses: EpisodeDiagnoses;
	private hasDiagnosesAssociatedSubject = new BehaviorSubject<boolean>(false);
	hasDiagnosesAssociated$ = this.hasDiagnosesAssociatedSubject.asObservable();

	constructor() { }

	setEpisodeDiagnoses(diagnoses: EpisodeDiagnoses) {
		this.episodeDiagnoses = diagnoses;
		this.hasDiagnosesAssociatedSubject.next(!!diagnoses.main || !!diagnoses.others.length)
	}

	getEpisodeDiagnoses(): DiagnosisDto[] {
		const main = this.episodeDiagnoses.main;
		const allEpisodeDiagnosis = deepClone(this.episodeDiagnoses.others);
		main && allEpisodeDiagnosis.push(main);
		return allEpisodeDiagnosis;
	}

	resetDiagnoses() {
		this.episodeDiagnoses = null;
		this.hasDiagnosesAssociatedSubject.next(false);
	}

}

interface EpisodeDiagnoses {
	main: HealthConditionDto,
	others: DiagnosisDto[],
}