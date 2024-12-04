import { ReferenceEditionPopUpData, ReferenceEditionPopUpComponent } from '@access-management/dialogs/reference-edition-pop-up/reference-edition-pop-up.component';
import { ReferencePermissionCombinationService } from '@access-management/services/reference-permission-combination.service';
import { Component, EventEmitter, Input, Output } from '@angular/core';
import { ReferenceCompleteDataDto } from '@api-rest/api-model';
import { InstitutionalNetworkReferenceReportService } from '@api-rest/services/institutional-network-reference-report.service';
import { InstitutionalReferenceReportService } from '@api-rest/services/institutional-reference-report.service';
import { ButtonType } from '@presentation/components/button/button.component';
import { DialogService, DialogWidth } from '@presentation/services/dialog.service';
import { Observable, switchMap } from 'rxjs';

const AUDIT = 4;
const WAITING_APPROVAL = 0;
@Component({
    selector: 'app-edit-reference',
    templateUrl: './edit-reference.component.html',
    styleUrls: ['./edit-reference.component.scss']
})
export class EditReferenceComponent {

    ButtonType = ButtonType;
    @Input() isButton: boolean;
    @Input() referenceCompleteData: ReferenceCompleteDataDto;
    @Output() newReferenceDetail: EventEmitter<Observable<ReferenceCompleteDataDto>> = new EventEmitter<Observable<ReferenceCompleteDataDto>>;

    constructor(
        private dialogService: DialogService<ReferenceEditionPopUpComponent>,
        private readonly institutionalNetworkReferenceReportService: InstitutionalNetworkReferenceReportService,
        private readonly institutionalReferenceReportService: InstitutionalReferenceReportService,
        public permissionService: ReferencePermissionCombinationService
    ) { }

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
                if (this.isButton) {
                    if (this.permissionService.isRoleGestor)
                        this.institutionalNetworkReferenceReportService.changeReferenceRegulationState(this.referenceCompleteData.reference.id, AUDIT)
                        .pipe(switchMap(changed => {
                            if (changed)
                                return this.institutionalNetworkReferenceReportService.changeReferenceApprovalState(this.referenceCompleteData.reference.id, WAITING_APPROVAL)
                        })).subscribe(updated => {
                            if (updated)
                                this.newReferenceDetail.emit(this.institutionalNetworkReferenceReportService.getReferenceDetail(this.referenceCompleteData.reference.id));
                        });
                    else
                        this.institutionalReferenceReportService.changeReferenceRegulationStateAsGestorInstitucional(this.referenceCompleteData.reference.id, AUDIT)
                        .pipe(switchMap(changed => {
                            if (changed)
                                return this.institutionalReferenceReportService.changeReferenceApprovalStateAsGestorInstitucional(this.referenceCompleteData.reference.id, WAITING_APPROVAL)
                        })).subscribe(updated => {
                            if (updated)
                                this.newReferenceDetail.emit(this.institutionalReferenceReportService.getReferenceDetail(this.referenceCompleteData.reference.id));
                        });
                }
                else 
                    this.newReferenceDetail.emit(this.institutionalNetworkReferenceReportService.getReferenceDetail(this.referenceCompleteData.reference.id));
            }
		});
    }

}
