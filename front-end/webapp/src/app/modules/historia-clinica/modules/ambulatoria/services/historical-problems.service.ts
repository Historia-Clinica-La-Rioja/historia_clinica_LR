import { Injectable } from '@angular/core';
import { ReplaySubject, Observable } from 'rxjs';
import { tap } from 'rxjs/operators';
import { HistoricalProblemsFilter } from '../components/historical-problems-filters/historical-problems-filters.component';
import { pushIfNotExists } from '@core/utils/array.utils';
import { momentParseDateTime, momentParseDate } from '@core/utils/moment.utils';
import {HceGeneralStateService} from '@api-rest/services/hce-general-state.service';

@Injectable()
export class HistoricalProblemsService {

	public specialities: Speciality[] = [];
  	public professionals: Professional[] = [];
	public problems: Problem[] = [];

	private subject = new ReplaySubject<any[]>(1);
	private originalHistoricalProblems = [];

	constructor(
		private readonly hceGeneralStateService: HceGeneralStateService,
  	) { }

	public getHistoricalProblems(): Observable<any[]> {
		if (!this.originalHistoricalProblems.length) {
			/*this.hceGeneralStateService.getBedsSummary().pipe(
				tap((bedsSummary: void[]) => this.filterOptions(bedsSummary))
			).subscribe(data => {
				this.originalHistoricalProblems = data;
				this.sendHistoricalProblems(this.originalHistoricalProblems);
			});*/
		}
		return this.subject.asObservable();
	}

	public sendHistoricalProblems(bedManagement) {
		this.subject.next(bedManagement);
	}

	public sendHistoricalProblemsFilter(newFilter: HistoricalProblemsFilter) {
		const historichalProblemsCopy = [...this.originalHistoricalProblems];
		const result = historichalProblemsCopy.filter(historicalProblem => (this.filterBySpeciality(newFilter, historicalProblem)
																	&& this.filterByProfessional(newFilter, historicalProblem)
																	&& this.filterByProblem(newFilter, historicalProblem)
																	&& this.filterByConsultationDate(newFilter, historicalProblem)));
		this.subject.next(result);
	}

	private filterBySpeciality(filter: HistoricalProblemsFilter, problem): boolean {
		return (filter.speciality ? problem.clinicalSpecialty.id === filter.speciality : true);
	}

	private filterByProfessional(filter: HistoricalProblemsFilter, problem): boolean {
		return (filter.professional ? problem.professional.id === filter.professional : true);
	}

	private filterByProblem(filter: HistoricalProblemsFilter, problem): boolean {
		return (filter.problem ? problem.problem.id === filter.problem : true);
	}

	private filterByConsultationDate(filter: HistoricalProblemsFilter, problem): boolean {
		return (filter.consultationDate ? problem.consultationDate ? momentParseDateTime(problem.consultationDate).isSameOrBefore(momentParseDate(filter.consultationDate)) : false : true);
	}

	public getFilterOptions() {
		return {
			specialities: this.specialities,
			professionals: this.professionals,
			problems: this.problems
		};
	}

	private filterOptions(bedsSummary): void {
		bedsSummary.forEach(bedSummary => {

			this.specialities = pushIfNotExists(this.specialities, {specialityId: bedSummary.clinicalSpecialty.id, specialityDescription: bedSummary.clinicalSpecialty.name}, this.compareSpeciality)

			this.professionals = pushIfNotExists(this.professionals, {professionalId: bedSummary.professional.id, professionalDescription: bedSummary.professional.description}, this.compareProfessional);

			this.problems = pushIfNotExists(this.problems, {problemId: bedSummary.problems.id, problemDescription: bedSummary.problems.description}, this.compareProblems);
		});
	}

	private compareSpeciality(speciality: Speciality, speciality2: Speciality): boolean {
		return speciality.specialityId === speciality2.specialityId;
	}

	private compareProfessional(professional: Professional, professional2: Professional): boolean {
		return professional.professionalId === professional2.professionalId;
	}

	private compareProblems(problem: Problem, problem2: Problem): boolean {
		return problem.problemId === problem2.problemId;
	}

}

export class Speciality {
	specialityId: number;
	specialityDescription: string;
}

export class Professional {
	professionalId: number;
	professionalDescription: string;
}

export class Problem {
	problemId: number;
	problemDescription: string;
}
