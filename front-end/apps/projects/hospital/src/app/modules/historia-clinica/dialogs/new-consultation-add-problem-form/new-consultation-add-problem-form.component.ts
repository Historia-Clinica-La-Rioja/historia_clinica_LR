import { Component, Inject, OnInit } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { MIN_DATE } from '@core/utils/date.utils';
import { AmbulatoryConsultationProblemsService } from '@historia-clinica/services/ambulatory-consultation-problems.service';

@Component({
    selector: 'app-new-consultation-add-problem-form',
    templateUrl: './new-consultation-add-problem-form.component.html',
    styleUrls: ['./new-consultation-add-problem-form.component.scss']
})
export class NewConsultationAddProblemFormComponent implements OnInit {

    today = new Date();
    minDate = MIN_DATE;

    constructor(
        public dialogRef: MatDialogRef<NewConsultationAddProblemFormComponent>,
        @Inject(MAT_DIALOG_DATA) public readonly data: ProblemData,
    ) { }

    ngOnInit(): void {
        if (this.data?.editing) {
            this.data.ambulatoryConsultationProblemsService.loadForm(this.data.editIndex);
        }

        //Resetea el valor de fecha inicio para cuando se agregan varios problemas en la misma consulta
        if (!this.data.ambulatoryConsultationProblemsService.getForm().controls.fechaInicio?.value){
            this.data.ambulatoryConsultationProblemsService.resetStartDate();
        }
    }    

    fechaInicioChanged(date: Date) {
        this.data.ambulatoryConsultationProblemsService.getForm().controls.fechaInicio.setValue(date);
    }

    fechaFinChanged(date: Date) {
        this.data.ambulatoryConsultationProblemsService.getForm().controls.fechaFin.setValue(date);
    }

    editProblem(): void {
        this.data.ambulatoryConsultationProblemsService.editProblem(this.data.editIndex);
        this.dialogRef.close();
    }

    addProblem(): void {
        this.data.ambulatoryConsultationProblemsService.addToList(this.data.epidemiologicalReportFF);
        this.dialogRef.close(this.data.ambulatoryConsultationProblemsService.getProblemas());
    }

    resetForm(): void {
        this.data.ambulatoryConsultationProblemsService.resetForm();
    }
}

interface ProblemData {
    editing?: boolean,
    editIndex?: number,
    ambulatoryConsultationProblemsService: AmbulatoryConsultationProblemsService,
    severityTypes?: any[],
    epidemiologicalReportFF?: boolean,
    searchConceptsLocallyFF: boolean,
    isFromRecetaDigital?: boolean
}