import { Injectable } from '@angular/core';
import {
	ClinicalSpecialtyDto,
	HCEErrorProblemDto,
	HCEEvolutionSummaryDto,
	HCEReferenceDto
} from '@api-rest/api-model';
import { HceGeneralStateService } from "@api-rest/services/hce-general-state.service";
import { pushIfNotExists } from '@core/utils/array.utils';
import { Observable, ReplaySubject } from 'rxjs';
import { map, tap } from 'rxjs/operators';
import { HistoricalProblemsFilter } from '../components/historical-problems-filters/historical-problems-filters.component';
import { REFERENCE_STATES } from '../constants/reference-masterdata';
import { Detail } from '@presentation/components/details-section-custom/details-section-custom.component';
import { isSameDay } from 'date-fns';
import { AssociatedParameterizedFormInformation } from '../components/associated-parameterized-form-information/associated-parameterized-form-information.component';
import { toHistoricalProblems } from '@historia-clinica/mappers/problems.mapper';


@Injectable({ providedIn: 'root' })
export class HistoricalProblemsFacadeService {

	public specialties: ClinicalSpecialtyDto[] = [];
	public professionals: Professional[] = [];
	public problems: Problem[] = [];
	private readonly referenceStates = [REFERENCE_STATES.WITHOUT_REFERENCES, REFERENCE_STATES.WITH_REFERENCES, REFERENCE_STATES.WITH_COUNTERREFERENCE, REFERENCE_STATES.ALL];

	private historicalProblemsSubject = new ReplaySubject<HistoricalProblems[]>(1);
	private historicalProblems$: Observable<HistoricalProblems[]>;
	private historicalProblemsFilterSubject = new ReplaySubject<HistoricalProblemsFilter>(1);
	private historicalProblemsFilter$: Observable<HistoricalProblemsFilter>;
	private filterOptionsSubject = new ReplaySubject<FilterOptions>(1);
	private filterOptions$: Observable<FilterOptions>;
	private originalHistoricalProblems: HistoricalProblems[] = [];

	constructor(
		private readonly hceGeneralStateService: HceGeneralStateService,
	) {
		this.historicalProblems$ = this.historicalProblemsSubject.asObservable();
		this.historicalProblemsFilter$ = this.historicalProblemsFilterSubject.asObservable();
		this.filterOptions$ = this.filterOptionsSubject.asObservable();
	}

	setPatientId(patientId: number): void {
		this.resetProblemsFilter();
		this.loadEvolutionSummaryList(patientId);
	}

	public loadEvolutionSummaryList(patientId: number) {
		this.hceGeneralStateService.getEvolutionSummaryList(patientId).pipe(
			tap((hceEvolutionSummaryDto: HCEEvolutionSummaryDto[]) => this.filterOptions(hceEvolutionSummaryDto)),
			map((hceEvolutionSummaryDto: HCEEvolutionSummaryDto[]) => hceEvolutionSummaryDto.length ? toHistoricalProblems(hceEvolutionSummaryDto) : null)
		).subscribe(data => {
			this.originalHistoricalProblems = data;
			this.sendHistoricalProblems(this.originalHistoricalProblems);
			this.updateFilterOptions();
		});
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
		if (this.originalHistoricalProblems && this.originalHistoricalProblems.length) {
			const historichalProblemsCopy = [...this.originalHistoricalProblems];
			const result = historichalProblemsCopy.filter(historicalProblem => (this.filterBySpecialty(newFilter, historicalProblem)
				&& this.filterByProfessional(newFilter, historicalProblem)
				&& this.filterByProblem(newFilter, historicalProblem)
				&& this.filterByConsultationDate(newFilter, historicalProblem)
				&& this.filterByReference(newFilter, historicalProblem)));
			this.historicalProblemsSubject.next(result);
			this.historicalProblemsFilterSubject.next(newFilter);
		}
	}

	private filterBySpecialty(filter: HistoricalProblemsFilter, problem: HistoricalProblems): boolean {
		return (filter.specialty ? problem.specialtyId === filter.specialty : true);
	}

	private filterByProfessional(filter: HistoricalProblemsFilter, problem: HistoricalProblems): boolean {
		return (filter.professional ? problem.consultationProfessionalPersonId === filter.professional : true);
	}

	private filterByProblem(filter: HistoricalProblemsFilter, problem: HistoricalProblems): boolean {
		return (filter.problem ? problem.problemId === filter.problem : true);
	}

	private filterByConsultationDate(filter: HistoricalProblemsFilter, problem: HistoricalProblems): boolean {
		return (filter.consultationDate ? problem.consultationDate ? isSameDay(filter.consultationDate,problem.consultationDate,)  : false : true);
	}

	private filterByReference(filter: HistoricalProblemsFilter, problem: HistoricalProblems): boolean {
		switch (filter.referenceStateId) {
			case REFERENCE_STATES.WITH_REFERENCES:
				return ((problem.reference !== null) && (!problem.reference.filter(ref => ref.cancelled).length));

			case REFERENCE_STATES.WITHOUT_REFERENCES:
				return problem.reference === null;

			case REFERENCE_STATES.WITH_COUNTERREFERENCE:
				let result;
				problem.reference?.forEach(problemReference => {
					if (problemReference.counterReference?.counterReferenceNote !== undefined) {
						result = true;
					}
				})
				return result;

			default:
				return true;
		}
	}

	public updateFilterOptions() {
		this.filterOptionsSubject.next({
			specialties: this.specialties,
			professionals: this.professionals,
			problems: this.problems,
			referenceStates: this.referenceStates,
		});
	}

	private resetProblemsFilter() {
		this.historicalProblemsFilterSubject.next(null);
		this.problems = [];
	}

	public getFilterOptions(): Observable<FilterOptions> {
		return this.filterOptions$;
	}

	private filterOptions(hceEvolutionSummaryDto: HCEEvolutionSummaryDto[]): void {
		hceEvolutionSummaryDto.forEach(hceEvolutionSummaryDto => {

			if (hceEvolutionSummaryDto.clinicalSpecialty) {
				this.specialties = pushIfNotExists(this.specialties, hceEvolutionSummaryDto.clinicalSpecialty, this.compareSpecialty);
			}

			if (!hceEvolutionSummaryDto.healthConditions.length) {
				this.problems = pushIfNotExists(this.problems, { problemId: 'Problema no informado', problemDescription: 'Problema no informado' }, this.compareProblems);
			} else {
				hceEvolutionSummaryDto.healthConditions.forEach(oe => {
					this.problems = pushIfNotExists(this.problems, { problemId: oe.snomed.sctid, problemDescription: oe.snomed.pt }, this.compareProblems);
				});
			}

			this.professionals = pushIfNotExists(this.professionals, { personId: hceEvolutionSummaryDto.professional.person.id, professionalId: hceEvolutionSummaryDto.professional.id, professionalDescription: `${hceEvolutionSummaryDto.professional.person.fullName}` }, this.compareProfessional);

		});
	}

	private compareSpecialty(specialty: ClinicalSpecialtyDto, specialty2: ClinicalSpecialtyDto): boolean {
		return specialty.id === specialty2.id;
	}

	private compareProfessional(professional: Professional, professional2: Professional): boolean {
		return professional.personId === professional2.personId;
	}

	private compareProblems(problem: Problem, problem2: Problem): boolean {
		return problem.problemId === problem2.problemId;
	}

}

export class Professional {
	personId: number;
	professionalId: number;
	professionalDescription: string;
}

export class Problem {
	problemId: string;
	problemDescription: string;
}

export class HistoricalProblems {
	consultationDate: Date;
	consultationEvolutionNote: string;
	consultationProfessionalId: number;
	consultationProfessionalPersonId: number;
	professionalFullName: string;
	institutionName: string;
	document: {
		id: number;
		filename: string;
	};
	problemId: string;
	problemPt: string;
	specialtyId: number;
	specialtyPt: string;
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
	reference: HCEReferenceDto[];
	markedAsError?: boolean;
	color?: string;
	errorProblem?: HCEErrorProblemDto;
	headerInfoDetails?: Detail[];
	professionalsThatDidNotSignAmount: number;
	professionalsThatSignedNames: string[];
	parameterizedForms: AssociatedParameterizedFormInformation[];
}

export interface FilterOptions {
	specialties: ClinicalSpecialtyDto[],
	professionals: Professional[],
	problems: Problem[],
	referenceStates: REFERENCE_STATES[],
}