import { ReferenceEditionPopUpData, ReferenceEditionPopUpComponent } from '@access-management/dialogs/reference-edition-pop-up/reference-edition-pop-up.component';
import { Component, Input, OnInit } from '@angular/core';
import { ReferenceCompleteDataDto } from '@api-rest/api-model';
import { DialogService, DialogWidth } from '@presentation/services/dialog.service';

@Component({
    selector: 'app-edit-reference',
    templateUrl: './edit-reference.component.html',
    styleUrls: ['./edit-reference.component.scss']
})
export class EditReferenceComponent implements OnInit {

    @Input() referenceCompleteData: ReferenceCompleteDataDto;

    constructor(
        private dialogService: DialogService<ReferenceEditionPopUpComponent>,
    ) { }

    ngOnInit(): void {
    }

    openEditDialog() {
        const referenceEditionData: ReferenceEditionPopUpData = {
			referenceDataDto: this.referenceCompleteData.reference,
			referencePatientDto: this.referenceCompleteData.patient,
            isGestor: true
		}
		const editDialogRef = this.dialogService.open(ReferenceEditionPopUpComponent,
			{ dialogWidth: DialogWidth.MEDIUM },
            referenceEditionData
		)
		editDialogRef.afterClosed().subscribe(edited => {
			console.log('se edito la info?: ', edited)
		});
    }

}
