import { Component, OnInit, OnChanges, Input, Output, EventEmitter } from '@angular/core';
import { UntypedFormGroup, Validators } from '@angular/forms';
import { CareLineDto, ClinicalSpecialtyDto, ReferenceProblemDto } from '@api-rest/api-model';
import { CareLineService } from '@api-rest/services/care-line.service';
import { ClinicalSpecialtyService } from '@api-rest/services/clinical-specialty.service';
import { Observable } from 'rxjs';
import { ReferenceProblemsService } from '../../services/reference-problems.service';

@Component({
    selector: 'app-carelines-and-specialties-reference',
    templateUrl: './carelines-and-specialties-reference.component.html',
    styleUrls: ['./carelines-and-specialties-reference.component.scss']
})
export class CarelinesAndSpecialtiesReferenceComponent implements OnInit, OnChanges {

    @Input() formReference: UntypedFormGroup;
    @Input() set updateFormFields(updateSpecialtiesAndCarelineFields: boolean) {
        if (updateSpecialtiesAndCarelineFields) {
            this.setInformation();
        }
    }
    @Input() set clearCarelinesAndSpecialties(clearCarelinesAndSpecialtiesFields: boolean) {
        if (clearCarelinesAndSpecialtiesFields) {
            this.clearFormFields();
        }
    }

    @Output() resetControls = new EventEmitter();

    specialties$: Observable<ClinicalSpecialtyDto[]>;
    DEFAULT_RADIO_OPTION = '0';
    careLines: CareLineDto[];

    constructor(
        private readonly careLineService: CareLineService,
        private readonly clinicalSpecialty: ClinicalSpecialtyService,
        private readonly referenceProblemsService: ReferenceProblemsService) { }

    ngOnInit(): void {
        this.subscribesToChangesInForm();
    }

    ngOnChanges(): void {
        this.resetControls.emit();
    }

    setSpecialtyCareLine(): void {
        const careLine = this.formReference.value.careLine;
        if (careLine) {
            this.formReference.controls.clinicalSpecialtyId.enable();
            this.formReference.controls.clinicalSpecialtyId.setValidators([Validators.required]);
            this.formReference.updateValueAndValidity();
            this.specialties$ = this.clinicalSpecialty.getAllByDestinationInstitution(careLine.id, this.formReference.value.institutionDestinationId);
        }
    }

    setInformation() {
        if (!this.formReference.value.institutionDestinationId) {
            this.clearFormFields();
            return;
        }
        if (this.formReference.value.searchByCareLine === this.DEFAULT_RADIO_OPTION)
            this.setCareLines();
        else
            this.setSpecialties();
    }

    private setCareLines() {
        const problemSnomedIds: string[] = this.referenceProblemsService.mapProblems().map(problem => problem.snomed.sctid);
        const institutionId = this.formReference.value.institutionDestinationId;
        if (!problemSnomedIds.length || !institutionId) {
            this.formReference.controls.careLine.disable();
        }
        if (problemSnomedIds.length && institutionId) {
            this.formReference.controls.careLine.enable();
            this.formReference.controls.careLine.updateValueAndValidity();
            this.careLineService.getByProblemSnomedIdsAndInstitutionId(institutionId, problemSnomedIds).subscribe(careLines => this.careLines = careLines);
        }
        this.formReference.controls.clinicalSpecialtyId.disable();
    }

    private setSpecialties() {
        const institutionId = this.formReference.value.institutionDestinationId;
        if (institutionId) {
            this.formReference.controls.clinicalSpecialtyId.enable();
            this.specialties$ = this.clinicalSpecialty.getClinicalSpecialtyByInstitution(institutionId);
            this.formReference.controls.clinicalSpecialtyId.updateValueAndValidity();
        }
    }

    private subscribesToChangesInForm() {
        this.formReference.controls.searchByCareLine.valueChanges.subscribe(option => {
            if (option === this.DEFAULT_RADIO_OPTION) {
                this.updateClinicalSpecialtyFormField();
                this.setCareLines();
            } else {
                this.updateCareLineFormField();
                this.setSpecialties();
            }
        });
    }

    private updateClinicalSpecialtyFormField() {
        this.formReference.controls.careLine.setValidators([Validators.required]);
        this.formReference.controls.clinicalSpecialtyId.setValue(null);
        this.formReference.controls.clinicalSpecialtyId.disable();
        this.formReference.controls.clinicalSpecialtyId.updateValueAndValidity();
    }

    private updateCareLineFormField() {
        this.formReference.controls.careLine.removeValidators([Validators.required]);
        this.formReference.controls.careLine.setValue(null);
        this.formReference.controls.careLine.disable();
        this.formReference.controls.careLine.updateValueAndValidity();
    }

    private clearFormFields() {
        if (this.careLines?.length) {
            this.careLines = [];
            this.formReference.controls.careLine.setValue(null);
            this.formReference.controls.careLine.updateValueAndValidity();
        }
        this.specialties$?.subscribe(specialties => {
            if (specialties.length) {
                this.formReference.controls.clinicalSpecialtyId.setValue(null);
                this.formReference.controls.clinicalSpecialtyId.updateValueAndValidity();
            }
        });

        disableInputs(this.formReference, this.referenceProblemsService.mapProblems());

        function disableInputs(formReference: UntypedFormGroup, referenceProblemDto: ReferenceProblemDto[]) {
            if (!formReference.value.institutionDestinationId) {
                formReference.controls.clinicalSpecialtyId.disable();
            }
            if (!referenceProblemDto.length || !formReference.value.institutionDestinationId) {
                formReference.controls.careLine.disable();
            }
        }
    }
}
