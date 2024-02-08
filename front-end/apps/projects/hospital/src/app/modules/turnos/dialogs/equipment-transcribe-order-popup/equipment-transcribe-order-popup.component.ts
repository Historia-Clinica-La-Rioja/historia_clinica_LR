import { Component, Inject, OnInit } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { SnomedDto, SnomedECL, TranscribedServiceRequestDto } from '@api-rest/api-model';
import { HCEHealthConditionDto } from '@api-rest/api-model';
import { HceGeneralStateService } from '@api-rest/services/hce-general-state.service';
import { hasError } from '@core/utils/form.utils';
import { PrescripcionesService } from '@historia-clinica/modules/ambulatoria/services/prescripciones.service';
import { TranslateService } from '@ngx-translate/core';
import { TranscribedOrderInfoEdit } from '@turnos/components/medical-order-input/medical-order-input.component';
import { Observable, map, of, switchMap } from 'rxjs';

@Component({
    selector: 'app-equipment-transcribe-order-popup',
    templateUrl: './equipment-transcribe-order-popup.component.html',
    styleUrls: ['./equipment-transcribe-order-popup.component.scss']
})
export class EquipmentTranscribeOrderPopupComponent implements OnInit {

    transcribeOrderForm: FormGroup<TranscribedFormModel>;
    public readonly hasError = hasError;
    readonly studyECL = SnomedECL.PROCEDURE;
    readonly problemECL = SnomedECL.DIAGNOSIS;
    selectedStudy: SnomedDto = null;
    selectedProblem: SnomedDto = null;
    healthProblems = null;
    selectedFiles: File[] = [];
    selectedFilesShow: any[] = [];
    filesExtension = false;
    allowedExtensions = ['jpg','jpeg','png','pdf'];

    constructor(
        public dialogRef: MatDialogRef<EquipmentTranscribeOrderPopupComponent>,
        @Inject(MAT_DIALOG_DATA) public data: TranscribedOrderInfoEdit,
        private readonly formBuilder: FormBuilder,
		private readonly hceGeneralStateService: HceGeneralStateService,
		private readonly prescriptionService: PrescripcionesService,
		private readonly translateService: TranslateService
        ) { }

    ngOnInit(): void {
        this.transcribeOrderForm = this.formBuilder.group<TranscribedFormModel>({
            study: new FormControl (null, Validators.required),
            assosiatedProblem: new FormControl (null, Validators.required),
            professional: new FormControl (null, Validators.required),
            institution: new FormControl (null, Validators.required),
            observations:  new FormControl (null)
        });
        if (this.data.transcribedOrder){
            this.selectedStudy = this.data.transcribedOrder.study;
            this.selectedProblem = this.data.transcribedOrder.problem;
            this.selectedFiles = this.data.transcribedOrder.selectedFiles;
            this.selectedFilesShow = this.data.transcribedOrder.selectedFilesShow;
            this.setFormValues(this.data.transcribedOrder);
        }

        this.getPatientHealthProblems();
    }

    private setFormValues(order){
        this.transcribeOrderForm.controls.study.setValue(order.study.pt)
        this.transcribeOrderForm.controls.assosiatedProblem.setValue(order.problem.pt)
        this.transcribeOrderForm.controls.professional.setValue(order.professional)
        this.transcribeOrderForm.controls.institution.setValue(order.institution)
        this.transcribeOrderForm.controls.observations.setValue(order.observations)
    }

    private getPatientHealthProblems() {
        this.hceGeneralStateService.getActiveProblems(this.data.patientId).subscribe((activeProblems: HCEHealthConditionDto[]) => {
			const activeProblemsList = activeProblems.map(problem => ({id: problem.id, description: problem.snomed.pt, sctId: problem.snomed.sctid}));

			this.hceGeneralStateService.getChronicConditions(this.data.patientId).subscribe((chronicProblems: HCEHealthConditionDto[]) => {
				const chronicProblemsList = chronicProblems.map(problem => ({id: problem.id, description: problem.snomed.pt,  sctId: problem.snomed.sctid}));
				const healthProblems = activeProblemsList.concat(chronicProblemsList);
                this.healthProblems = healthProblems;
			});
		});
    }

    saveOrder() {
        let orderProfessionalName = this.transcribeOrderForm.controls.professional?.value;
        let orderInstitutionName = this.transcribeOrderForm.controls.institution?.value;

        this.checkForOrderDeletion();

        let transcribedData: TranscribedServiceRequestDto = {
			study: this.selectedStudy,
			healthCondition: this.selectedProblem,
			healthcareProfessionalName: orderProfessionalName,
			institutionName: orderInstitutionName,
            observations:  this.transcribeOrderForm.controls.observations.value
		}

        this.prescriptionService.createTranscribedOrder(this.data.patientId, transcribedData)
            .pipe(
                switchMap(serviceRequestId => this.handleAttachedFilesByCondition(serviceRequestId)),
                switchMap(serviceRequestId => this.buildTranscribedOrderContext(serviceRequestId)))
            .subscribe(transcribedOrderContext => {
                this.dialogRef.close({
                    transcribeOrder: transcribedOrderContext.contextInfo,
                    order: {
                        serviceRequestId: transcribedOrderContext.contextInfo.serviceRequestId,
                        studyName: this.selectedStudy.pt,
                        displayText: `${transcribedOrderContext.title} - ${this.selectedStudy.pt}`,
                        isTranscribed: true
                    }
                })
            })
    }

    private buildTranscribedOrderContext(serviceRequestId: number): Observable<TranscribeOrderPopupContext> {
        let orderProfessional = this.transcribeOrderForm.controls.professional?.value;
        let orderInstitution = this.transcribeOrderForm.controls.institution?.value;
        let orderObservations = this.transcribeOrderForm.controls.observations?.value;

        let text = 'image-network.appointments.medical-order.TRANSCRIBED_ORDER';
        return this.translateService.get(text)
            .pipe(map(translatedText => {
                return {
                    contextInfo: {
                        study: this.selectedStudy,
                        serviceRequestId: serviceRequestId,
                        problem: this.selectedProblem,
                        professional: orderProfessional,
                        institution: orderInstitution,
                        selectedFiles: this.selectedFiles,
                        selectedFilesShow: this.selectedFilesShow,
                        observations: orderObservations
                    },
                    title: translatedText
                }
            }))
    }

    private handleAttachedFilesByCondition(serviceRequestId: number): Observable<number> {
        const sourceExistsAttachedFiles$: Observable<number> = this.prescriptionService.saveAttachedFiles(this.data.patientId, serviceRequestId, this.selectedFiles).pipe(map( _ => serviceRequestId ))
        const source$ = this.selectedFiles.length > 0 ?  sourceExistsAttachedFiles$ : of(serviceRequestId)
        return source$
    }

    checkForOrderDeletion(){
        if (this.data.transcribedOrder){
            let serviceRequestId = this.data.transcribedOrder.serviceRequestId;
            this.prescriptionService.deleteTranscribedOrder(this.data.patientId, serviceRequestId).subscribe();
        }
    }

    private checkFileExtensions(){
        this.selectedFilesShow.map(file => {
            let fileExt = file.split(".").pop();
            if (!this.allowedExtensions.includes(fileExt)){
                this.filesExtension = true;
            }
        })
    }


    isFormValid(): boolean {
        if (this.selectedFiles.length > 0)
            return !this.filesExtension && this.transcribeOrderForm.valid
        return this.transcribeOrderForm.valid
    }

    onFileSelected($event){
        Array.from($event.target.files).forEach((file: File) => {
			this.selectedFiles.push(file);
			this.selectedFilesShow.push(file.name);
		});
        this.checkFileExtensions();
    }

    removeSelectedFile(index): void {
        this.filesExtension = false;
		this.selectedFiles.splice(index, 1);
		this.selectedFilesShow.splice(index, 1);
        this.checkFileExtensions();
	}

    handleStudySelected(study) {
		this.selectedStudy = study;
		this.transcribeOrderForm.controls.study.setValue(this.getStudyDisplayName());
	}

    handleProblemSelected(problem) {
		this.selectedProblem = problem;
		this.transcribeOrderForm.controls.assosiatedProblem.setValue(this.getProblemDisplayName());
	}

    getStudyDisplayName(): string {
		return this.selectedStudy?.pt;
	}

    getProblemDisplayName(): string {
		return this.selectedProblem?.pt;
	}

    resetStudySelector() {
        this.selectedStudy = null;
        this.transcribeOrderForm.controls.study.setValue(null);
    }

    resetProblemSelector() {
        this.selectedProblem = null;
        this.transcribeOrderForm.controls.assosiatedProblem.setValue(null);
    }
}


export interface TranscribeOrderPopupContext {
    contextInfo: InfoTranscribeOrderPopup,
    title: string
}

export interface InfoTranscribeOrderPopup {
    study: SnomedDto
    serviceRequestId: number
    problem: SnomedDto
    professional: string
    institution: string
    selectedFiles: File[]
    selectedFilesShow: File[]
    observations: string
}

export interface TranscribedFormModel {
    study:  FormControl<string>;
    assosiatedProblem: FormControl<string>,
    professional: FormControl<string>,
    institution:  FormControl<string>,
    observations:  FormControl<string>
}
