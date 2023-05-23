import { Component, OnInit, Input, Output, EventEmitter } from '@angular/core';
import { UntypedFormGroup, UntypedFormControl } from '@angular/forms';
import { AddressDto, DepartmentDto, InstitutionBasicInfoDto } from '@api-rest/api-model';
import { AddressMasterDataService } from '@api-rest/services/address-master-data.service';
import { InstitutionService } from '@api-rest/services/institution.service';
import { ContextService } from '@core/services/context.service';
import { forkJoin, Observable, of } from 'rxjs';
import { ReferenceOriginInstitutionService } from '../../services/reference-origin-institution.service';
import { ReferenceProblemsService } from '../../services/reference-problems.service';

@Component({
    selector: 'app-origin-institution-reference',
    templateUrl: './origin-institution-reference.component.html',
    styleUrls: ['./origin-institution-reference.component.scss']
})
export class OriginInstitutionReferenceComponent implements OnInit {

    @Input() formReference: UntypedFormGroup;
    @Input() data: any;
    @Output() updateSpecialtiesAndCarelineFields = new EventEmitter();

    problemsList$: Observable<any[]>;
    referenceProblems: any[] = [];

    constructor(
        private readonly institutionService: InstitutionService,
        private readonly adressMasterData: AddressMasterDataService,
        private readonly contextService: ContextService,
        private readonly referenceOriginInstitutionService: ReferenceOriginInstitutionService,
        private readonly referenceProblemsService: ReferenceProblemsService) { }

    ngOnInit(): void {
        this.setProblems();

        this.disableInputs();

        this.referenceOriginInstitutionService.originInstitutionInfo$.subscribe(originInfo => {
            if (originInfo){
                this.loadOriginInstitutionInfo(originInfo);
            }
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
        this.problemsList$ = of(this.referenceProblemsService.setProblems(this.data));
    }

    get associatedProblemsControls(): UntypedFormControl {
        return this.formReference.get('problems') as UntypedFormControl;
    }

    setProblemsReference(problemsArray: string[]) {
        this.referenceProblemsService.setReferenceProblems(problemsArray);
        this.referenceProblems = this.referenceProblemsService.getReferenceProblems();
        this.updateSpecialtiesAndCarelineFields.emit();
    }
}
