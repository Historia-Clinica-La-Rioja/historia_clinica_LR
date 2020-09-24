import { Injectable } from '@angular/core';
import { ReplaySubject, Observable } from 'rxjs';
import { map, tap } from 'rxjs/operators';
import { HistoricalProblemsFilter } from '../components/historical-problems-filters/historical-problems-filters.component';
import { pushIfNotExists } from '@core/utils/array.utils';
import { momentParseDate } from '@core/utils/moment.utils';
import { OutpatientEvolutionSummaryDto } from '@api-rest/api-model';
import { OutpatientConsultationService } from './../../../../api-rest/services/outpatient-consultation.service';
import { MapperService } from './../../../../presentation/services/mapper.service';

@Injectable()
export class HistoricalProblemsFacadeService {

  	public specialities: Speciality[] = [];
  	public professionals: Professional[] = [];
	public problems: Problem[] = [];

	private historicalProblemsSubject = new ReplaySubject<HistoricalProblems[]>(1);
	private historicalProblems$: Observable<HistoricalProblems[]>;
	private historicalProblemsFilterSubject = new ReplaySubject<HistoricalProblemsFilter>(1);
	private historicalProblemsFilter$: Observable<HistoricalProblemsFilter>;
	private originalHistoricalProblems: HistoricalProblems[] = [];

	constructor(
		private readonly outpatientConsultationService: OutpatientConsultationService,
		private readonly mapperService: MapperService,
  	) {
		this.historicalProblems$ = this.historicalProblemsSubject.asObservable();
		this.historicalProblemsFilter$ = this.historicalProblemsFilterSubject.asObservable();
	}

	setPatientId(patientId: number): void {
		if (!this.originalHistoricalProblems.length) {
			this.outpatientConsultationService.getEvolutionSummaryList(patientId).pipe(
				tap((outpatientEvolutionSummary: OutpatientEvolutionSummaryDto[]) => this.filterOptions(outpatientEvolutionSummary)),
				map((outpatientEvolutionSummary: OutpatientEvolutionSummaryDto[]) => outpatientEvolutionSummary.length ? this.mapperService.toHistoricalProblems(outpatientEvolutionSummary) : null)
			).subscribe(data => {
				this.originalHistoricalProblems = data;
				this.sendHistoricalProblems(this.originalHistoricalProblems);
			});
		}
	}

	public getHistoricalProblems(): Observable<HistoricalProblems[]> {
		return this.historicalProblems$;
	}

	public getHistoricalProblemsFilter(): Observable<HistoricalProblemsFilter> {
		return this.historicalProblemsFilter$;
	}

	public sendHistoricalProblems(outpatientEvolutionSummary: HistoricalProblems[]): void {
		this.historicalProblemsSubject.next(outpatientEvolutionSummary);
	}

	public sendHistoricalProblemsFilter(newFilter: HistoricalProblemsFilter): void {
		const historichalProblemsCopy = [...this.originalHistoricalProblems];
		const result = historichalProblemsCopy.filter(historicalProblem => (this.filterByProfessional(newFilter, historicalProblem)
																	&& this.filterByProblem(newFilter, historicalProblem)
																	&& this.filterByConsultationDate(newFilter, historicalProblem)));
		this.historicalProblemsSubject.next(result);
		this.historicalProblemsFilterSubject.next(newFilter);
	}

	private filterByProfessional(filter: HistoricalProblemsFilter, problem: HistoricalProblems): boolean {
		return (filter.professional ? problem.consultationProfessionalId === filter.professional : true);
	}

	private filterByProblem(filter: HistoricalProblemsFilter, problem: HistoricalProblems): boolean {
		return (filter.problem ? Number(problem.problemId) === Number(filter.problem) : true);
	}

	private filterByConsultationDate(filter: HistoricalProblemsFilter, problem: HistoricalProblems): boolean {
		return (filter.consultationDate ? problem.consultationDate ? momentParseDate(problem.consultationDate).isSame(momentParseDate(filter.consultationDate)) : false : true);
	}

	public getFilterOptions() {
		return {
			specialities: this.specialities,
			professionals: this.professionals,
			problems: this.problems
		};
	}

	private filterOptions(outpatientEvolutionSummary: OutpatientEvolutionSummaryDto[]): void {
		outpatientEvolutionSummary.forEach(outpatientEvolution => {

			outpatientEvolution.healthConditions.forEach(oe => {
				this.problems = pushIfNotExists(this.problems, {problemId: oe.snomed.id, problemDescription: oe.snomed.pt}, this.compareProblems);
			});

			this.professionals = pushIfNotExists(this.professionals, {professionalId: outpatientEvolution.medic.id, professionalDescription: `${outpatientEvolution.medic.person.firstName} ${outpatientEvolution.medic.person.lastName}`} , this.compareProfessional);

		});
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
	problemId: string;
	problemDescription: string;
}

export class HistoricalProblems {
	consultationDate: string;
	consultationEvolutionNote: string;
	consultationProfessionalId: number;
	consultationProfessionalName: string;
	problemId: string;
	problemPt: string;
  	consultationReasons:
	{
		reasonId: string;
		reasonPt: string;
  	}[];
	consultationProcedures:
	{
		procedureDate: string;
		procedureId: string;
		procedurePt: string;
	}[];
}

