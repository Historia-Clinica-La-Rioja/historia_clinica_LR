import { Injectable } from "@angular/core";
import { HCEHealthConditionDto, ReferenceProblemDto, SharedSnomedDto } from "@api-rest/api-model";
import { mapToString } from "@api-rest/mapper/date-dto.mapper";
import { HceGeneralStateService } from "@api-rest/services/hce-general-state.service";
import { BehaviorSubject, forkJoin, Observable } from "rxjs";
import { HCEPersonalHistory } from "../dialogs/reference/reference.component";

@Injectable()

export class ReferenceProblemsService {

	problemsList: any[] = [];
    referenceProblems: ReferenceProblemDto[] = [];
	private problemSubject = new BehaviorSubject<ReferenceProblemDto[]>([]);

    constructor(private readonly hceGeneralStateService: HceGeneralStateService) { }

    mapProblems(): ReferenceProblemDto[] {
        return this.referenceProblems.map(problem => ({
            id: problem.id,
            snomed: problem.snomed,
        }));
    }

	firstProblem(): SharedSnomedDto {
		return  this.referenceProblems[0]?.snomed;
    }

    getReferenceProblems(): HCEPersonalHistory[] {
        let problems: HCEPersonalHistory[] = [];
        this.referenceProblems.forEach(referenceProblemDto => {
            const problemToAdd = this.problemsList.find(problem => problem.HCEHealthConditionDto.snomed.sctid === referenceProblemDto.snomed.sctid)
            problems.push(problemToAdd);

        });
        return problems;
    }

    setReferenceProblems(problemsArray: string[]) {
        if (problemsArray.length) {
            this.referenceProblems = (problemsArray.map(problem => ({
                id: this.problemsList.find(p => p.HCEHealthConditionDto.snomed.pt === problem)?.HCEHealthConditionDto.id,
                snomed: this.problemsList.find(p => p.HCEHealthConditionDto.snomed.pt === problem)?.HCEHealthConditionDto.snomed,
            })));
        }
        else {
            this.referenceProblems = [];
        }
		this.problemSubject.next(this.referenceProblems);
    }

	getProblems(): Observable<ReferenceProblemDto[]> {
		return this.problemSubject.asObservable();
	}

    setProblems(data: any): HCEPersonalHistory[] {

        this.problemsList = data.consultationProblems.map(consultationProblem => {
            return {
                HCEHealthConditionDto: this.buildPersonalHistoryDto(consultationProblem),
                chronic: consultationProblem.cronico,
            }
        });

        const activeProblems$ = this.hceGeneralStateService.getActiveProblems(data.patientId);

        const chronicProblems$ = this.hceGeneralStateService.getChronicConditions(data.patientId);

        forkJoin([activeProblems$, chronicProblems$]).subscribe(([activeProblems, chronicProblems]) => {
            const chronicProblemsHCEPersonalHistory = chronicProblems.map(chronicProblem => {
                return {
                    HCEHealthConditionDto: chronicProblem,
                    chronic: true,
                }
            });

            const activeProblemsHCEPersonalHistory = activeProblems.map(activeProblem => {
                return {
                    HCEHealthConditionDto: activeProblem,
                    chronic: null,
                }
            });

            const problems = [...activeProblemsHCEPersonalHistory, ...chronicProblemsHCEPersonalHistory];
            problems.forEach((problem: HCEPersonalHistory) => {
                const existProblem = this.problemsList.find(consultationProblem => consultationProblem.HCEHealthConditionDto.snomed.sctid === problem.HCEHealthConditionDto.snomed.sctid);
                if (!existProblem) {
                    this.problemsList.push(problem);
                }
            });
        });

        return this.problemsList;
    }

    private buildPersonalHistoryDto(problem): HCEHealthConditionDto {
        return {
            hasPendingReference: false,
            inactivationDate: null,
            severity: problem.codigoSeveridad,
            startDate: (problem.fechaInicio?.day) ? mapToString(problem.fechaInicio) : problem.fechaInicio,
            snomed: problem.snomed
        }
    }
}
