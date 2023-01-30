import { Component, OnInit, Input, Output, EventEmitter } from '@angular/core';
import { FormGroup, FormControl } from '@angular/forms';
import { AddressDto, DateDto, DepartmentDto, HCEPersonalHistoryDto, InstitutionBasicInfoDto, ReferenceProblemDto } from '@api-rest/api-model';
import { AddressMasterDataService } from '@api-rest/services/address-master-data.service';
import { HceGeneralStateService } from '@api-rest/services/hce-general-state.service';
import { InstitutionService } from '@api-rest/services/institution.service';
import { ContextService } from '@core/services/context.service';
import { forkJoin, Observable, of } from 'rxjs';
import { HCEPersonalHistory } from '../../dialogs/reference/reference.component';
import { ReferenceOriginInstitutionService } from '../../services/reference-origin-institution.service';

@Component({
    selector: 'app-origin-institution-reference',
    templateUrl: './origin-institution-reference.component.html',
    styleUrls: ['./origin-institution-reference.component.scss']
})
export class OriginInstitutionReferenceComponent implements OnInit {

    @Input() formReference: FormGroup;
    @Input() data: any;
    @Output() updateSpecialtiesAndCarelineFields = new EventEmitter();
    @Output() referenceProblemDto = new EventEmitter<ReferenceProblemDto[]>();
    @Output() problems = new EventEmitter<any[]>();

    problemsList$: Observable<any[]>;
	problemsList: any[] = [];

    constructor(		
		private readonly hceGeneralStateService: HceGeneralStateService,
        private readonly institutionService: InstitutionService,
        private readonly adressMasterData: AddressMasterDataService,
        private readonly contextService: ContextService,
        private readonly referenceOriginInstitutionService: ReferenceOriginInstitutionService) { }

    ngOnInit(): void {
        this.setProblems();

        this.disableInputs();

        this.referenceOriginInstitutionService.originInstitutionInfo$.subscribe(originInfo => {
            if (originInfo)
                this.loadOriginInstitutionInfo(originInfo);
        })
    }

    private loadOriginInstitutionInfo(institutionInfo: AddressDto) {
        if (institutionInfo.provinceId) {
            this.setOriginProvince(institutionInfo.provinceId);
            this.adressMasterData.getDepartmentById(institutionInfo.departmentId).subscribe((department: DepartmentDto) => {
                this.setOriginDepartment(department);
                this.institutionService.findByDepartmentId(department.id).subscribe((institutions: InstitutionBasicInfoDto[]) =>
                    this.setOriginInstitution(institutions)
                );
            });
        }
        else {
            if (institutionInfo.departmentId) {
                const information$: Observable<any>[] = [];
                information$.push(this.adressMasterData.getDepartmentById(institutionInfo.departmentId));
                information$.push(this.institutionService.findByDepartmentId(institutionInfo.departmentId));
                forkJoin(information$).subscribe(info => {
                    this.setOriginProvince(info[0].provinceId);
                    this.setOriginDepartment(info[0]);
                    this.setOriginInstitution(info[1]);
                });
            }
        }
    }

    private setOriginProvince(provinceId: number) {
        this.referenceOriginInstitutionService.getProvinceById(provinceId).subscribe(provinceOrigin => {
			this.formReference.controls.provinceOrigin.setValue(provinceOrigin.compareValue);
		})        
    }

    private setOriginDepartment(department: DepartmentDto) {
        this.formReference.controls.departmentOrigin.setValue(department.description);
        this.referenceOriginInstitutionService.setOriginDepartment(department);
    }

    private setOriginInstitution(institutions: InstitutionBasicInfoDto[]) {
        const institution = institutions.find((i: InstitutionBasicInfoDto) => i.id === this.contextService.institutionId);
        this.formReference.controls.institutionOrigin.setValue(institution.name);
    }

    private disableInputs() {
        this.formReference.controls.provinceOrigin.disable();
        this.formReference.controls.departmentOrigin.disable();
        this.formReference.controls.institutionOrigin.disable();
    }

    setProblems() {

		const consultationProblems = this.data.consultationProblems.map(consultationProblem => {
			return {
				hcePersonalHistoryDto: this.buildPersonalHistoryDto(consultationProblem),
				chronic: consultationProblem.cronico,
			}
		});

		consultationProblems.forEach(problem => this.problemsList.push(problem));

		const activeProblems$ = this.hceGeneralStateService.getActiveProblems(this.data.patientId);

		const chronicProblems$ = this.hceGeneralStateService.getChronicConditions(this.data.patientId);

		forkJoin([activeProblems$, chronicProblems$]).subscribe(([activeProblems, chronicProblems]) => {
			const chronicProblemsHCEPersonalHistory = chronicProblems.map(chronicProblem => {
				return {
					hcePersonalHistoryDto: chronicProblem,
					chronic: true,
				}
			});

			const activeProblemsHCEPersonalHistory = activeProblems.map(activeProblem => {
				return {
					hcePersonalHistoryDto: activeProblem,
					chronic: null,
				}
			});

			const problems = [...activeProblemsHCEPersonalHistory, ...chronicProblemsHCEPersonalHistory];
			problems.forEach((problem: HCEPersonalHistory) => {
				const existProblem = this.problemsList.find(consultationProblem => consultationProblem.hcePersonalHistoryDto.snomed.sctid === problem.hcePersonalHistoryDto.snomed.sctid);
				if (!existProblem) {
					this.problemsList.push(problem);
				}
			});
			this.problemsList$ = of(this.problemsList);
            this.problems.emit(this.problemsList);
		});
	}
    
	get associatedProblemsControls(): FormControl {
		return this.formReference.get('problems') as FormControl;
	}

	setProblemsReference(problemsArray: string[]) {
		if (problemsArray.length) {
			this.referenceProblemDto.emit(problemsArray.map(problem => ({
				id: this.problemsList.find(p => p.hcePersonalHistoryDto.snomed.pt === problem).hcePersonalHistoryDto.id,
				snomed: this.problemsList.find(p => p.hcePersonalHistoryDto.snomed.pt === problem).hcePersonalHistoryDto.snomed,
			})));
		}
		else {
			this.referenceProblemDto.emit([]);
		}
		this.updateSpecialtiesAndCarelineFields.emit();
	}

	private buildPersonalHistoryDto(problem): HCEPersonalHistoryDto {
		return {
			hasPendingReference: false,
			inactivationDate: null,
			severity: problem.codigoSeveridad,
			startDate: (problem.fechaInicio?.day) ? this.mapToString(problem.fechaInicio) : problem.fechaInicio,
			snomed: problem.snomed
		}
	}
    
	private mapToString(date: DateDto): string {
		return date.year.toString() + date.month.toString() + date.day.toString();

	}
}
