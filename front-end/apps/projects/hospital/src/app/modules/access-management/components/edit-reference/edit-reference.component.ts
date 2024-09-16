import { ReferenceEditionPopUpData, ReferenceEditionPopUpComponent } from '@access-management/dialogs/reference-edition-pop-up/reference-edition-pop-up.component';
import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { ReferenceCompleteDataDto } from '@api-rest/api-model';
import { InstitutionalNetworkReferenceReportService } from '@api-rest/services/institutional-network-reference-report.service';
import { DialogService, DialogWidth } from '@presentation/services/dialog.service';
import { Observable } from 'rxjs';

@Component({
    selector: 'app-edit-reference',
    templateUrl: './edit-reference.component.html',
    styleUrls: ['./edit-reference.component.scss']
})
export class EditReferenceComponent implements OnInit {

    @Input() referenceCompleteData: ReferenceCompleteDataDto;
    @Output() newReferenceDetail: EventEmitter<Observable<ReferenceCompleteDataDto>> = new EventEmitter<Observable<ReferenceCompleteDataDto>>;

    constructor(
        private dialogService: DialogService<ReferenceEditionPopUpComponent>,
        private readonly institutionalNetworkReferenceReportService: InstitutionalNetworkReferenceReportService,
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
			if (edited) {
                this.newReferenceDetail.emit(this.institutionalNetworkReferenceReportService.getReferenceDetail(this.referenceCompleteData.reference.id));
            }
		});
    }

}
