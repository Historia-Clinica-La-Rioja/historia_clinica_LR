import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { AnestheticReportPremedicationAndFoodIntakeService } from '../../services/anesthetic-report-premedication-and-food-intake.service';
import { FormGroup } from '@angular/forms';
import { TimeDto } from '@api-rest/api-model';

@Component({
    selector: 'app-anesthetic-report-pemedication',
    templateUrl: './anesthetic-report-pemedication.component.html',
    styleUrls: ['./anesthetic-report-pemedication.component.scss']
})
export class AnestheticReportPemedicationComponent {

    form: FormGroup;
    possibleTimesList: TimeDto[];

    viaArray = [{description: "Endovenosa", id: "1"}, {description: "Subcutanea", id: "2"}, {description: "Inhalatoria", id: "3"}]

    constructor(
        public dialogRef: MatDialogRef<AnestheticReportPemedicationComponent>,
        @Inject(MAT_DIALOG_DATA) public readonly data: PremedicationData,
    ) { 
        this.form = this.data.premedicationService.getForm();
        this.possibleTimesList = this.data.premedicationService.possibleTimesList;
    }

    close(): void {
        this.dialogRef.close()
        this.data.premedicationService.resetForm();
    }

    addPremedication(){
        this.data.premedicationService.addToList();
        this.dialogRef.close();
    }
}

interface PremedicationData {
    premedicationService: AnestheticReportPremedicationAndFoodIntakeService,
    searchConceptsLocallyFF: boolean,
}