import { Component, Inject, OnInit } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialog, MatDialogRef } from '@angular/material/dialog';
import { SnomedDto, SnomedECL, TranscribedServiceRequestDto } from '@api-rest/api-model';
import { HCEHealthConditionDto } from '@api-rest/api-model';
import { HceGeneralStateService } from '@api-rest/services/hce-general-state.service';
import { hasError } from '@core/utils/form.utils';
import { PrescripcionesService } from '@historia-clinica/modules/ambulatoria/services/prescripciones.service';
import { TranslateService } from '@ngx-translate/core';
import { TranscribedOrderInfoEdit } from '@turnos/components/medical-order-input/medical-order-input.component';
import { Observable, map, of, switchMap } from 'rxjs';
import { TranscribedStudyComponent } from '../transcribed-study/transcribed-study.component';
import { pushIfNotExists, removeFrom } from '@core/utils/array.utils';
import { getStudiesNames } from '@turnos/utils/appointment.utils';

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
    associatedStudies: SnomedDto[] = [];

    constructor(
        public dialogRef: MatDialogRef<EquipmentTranscribeOrderPopupComponent>,
        @Inject(MAT_DIALOG_DATA) public data: TranscribedOrderInfoEdit,
        private readonly dialog: MatDialog,
        private readonly formBuilder: FormBuilder,
		private readonly hceGeneralStateService: HceGeneralStateService,
		private readonly prescriptionService: PrescripcionesService,
		private readonly translateService: TranslateService
        ) { }

    ngOnInit(): void {
        this.transcribeOrderForm = this.formBuilder.group<TranscribedFormModel>({
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

    private setFormValues(order:InfoTranscribeOrderPopup){
        this.transcribeOrderForm.controls.assosiatedProblem.setValue(order.problem.pt)
        this.transcribeOrderForm.controls.professional.setValue(order.professional)
        this.transcribeOrderForm.controls.institution.setValue(order.institution)
        this.transcribeOrderForm.controls.observations.setValue(order.observations)
        this.associatedStudies = order.associatedStudies
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

        let transcribedData: TranscribedServiceRequestDto = {
            diagnosticReports: this.associatedStudies,
			healthCondition: this.selectedProblem,
			healthcareProfessionalName: orderProfessionalName,
			institutionName: orderInstitutionName,
            observations:  this.transcribeOrderForm.controls.observations.value,
            oldTranscribedOrderId: this.data.transcribedOrder?.serviceRequestId ?? null,
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
                        studyName: null,
                        displayText: getStudiesNames(this.associatedStudies.map(study => study.pt), transcribedOrderContext.title),
                        isTranscribed: true,
                        associatedStudies: this.associatedStudies
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
                        serviceRequestId: serviceRequestId,
                        problem: this.selectedProblem,
                        professional: orderProfessional,
                        institution: orderInstitution,
                        selectedFiles: this.selectedFiles,
                        selectedFilesShow: this.selectedFilesShow,
                        observations: orderObservations,
                        associatedStudies: this.associatedStudies
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

    private checkFileExtensions(){
        this.selectedFilesShow.map(file => {
            let fileExt = file.split(".").pop();
            if (!this.allowedExtensions.includes(fileExt)){
                this.filesExtension = true;
            }
        })
    }


    isFormValid(): boolean {
        const baseValidation = this.transcribeOrderForm.valid && this.associatedStudies.length > 0
        if (this.selectedFiles.length > 0)
            return !this.filesExtension && baseValidation
        return baseValidation
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


    resetProblemSelector() {
        this.selectedProblem = null;
        this.transcribeOrderForm.controls.assosiatedProblem.setValue(null);
    }

    addStudy() {
        this.dialog.open(TranscribedStudyComponent, {
            autoFocus: false,
            width: '30%',
            disableClose: true,
        }).afterClosed().subscribe(
            snomed => {
                if (snomed) {
                    this.associatedStudies = pushIfNotExists<any>(this.associatedStudies, snomed,
                        (first:SnomedDto , second: SnomedDto) => first.sctid === second.sctid );
                }
            }
        )
    }

    removeStudy(index: number) {
        this.associatedStudies = removeFrom<SnomedDto>(this.associatedStudies, index);
    }
}


export interface TranscribeOrderPopupContext {
    contextInfo: InfoTranscribeOrderPopup,
    title: string
}

export interface InfoTranscribeOrderPopup {
    study?: SnomedDto
    serviceRequestId: number
    problem: SnomedDto
    professional: string
    institution: string
    selectedFiles: File[]
    selectedFilesShow: File[]
    observations: string
    associatedStudies?: SnomedDto[],
}

export interface TranscribedFormModel {
    assosiatedProblem: FormControl<string>,
    professional: FormControl<string>,
    institution:  FormControl<string>,
    observations:  FormControl<string>
}
