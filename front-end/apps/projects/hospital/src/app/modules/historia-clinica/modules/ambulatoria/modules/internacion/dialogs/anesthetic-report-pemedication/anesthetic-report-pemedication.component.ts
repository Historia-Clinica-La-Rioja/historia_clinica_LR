import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { AnestheticReportPremedicationAndFoodIntakeService } from '../../services/anesthetic-report-premedication-and-food-intake.service';
import { FormGroup } from '@angular/forms';
import { MasterDataDto, TimeDto } from '@api-rest/api-model';
import { InternacionMasterDataService } from '@api-rest/services/internacion-master-data.service';
import { take } from 'rxjs';

@Component({
    selector: 'app-anesthetic-report-pemedication',
    templateUrl: './anesthetic-report-pemedication.component.html',
    styleUrls: ['./anesthetic-report-pemedication.component.scss']
})
export class AnestheticReportPemedicationComponent {

    form: FormGroup;
    possibleTimesList: TimeDto[];

    viasArray: MasterDataDto[];

    constructor(
        public dialogRef: MatDialogRef<AnestheticReportPemedicationComponent>,
        @Inject(MAT_DIALOG_DATA) public readonly data: PremedicationData,
        readonly internacionMasterDataService: InternacionMasterDataService,
    ) { 
        this.form = this.data.premedicationService.getForm();
        this.possibleTimesList = this.data.premedicationService.possibleTimesList;
        internacionMasterDataService.getViasPremedication().pipe(take(1)).subscribe(vias => this.viasArray = vias);
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