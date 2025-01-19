import { AdministrativeReferenceClosurePopupComponent } from '@access-management/dialogs/administrative-reference-closure-popup/administrative-reference-closure-popup.component';
import { Component, Input, OnInit, Output, EventEmitter } from '@angular/core';
import { DialogService, DialogWidth } from '@presentation/services/dialog.service';
@Component({
    selector: 'app-administrative-reference-closure',
    templateUrl: './administrative-reference-closure.component.html',
    styleUrls: ['./administrative-reference-closure.component.scss']
})
export class AdministrativeReferenceClosureComponent implements OnInit {

    @Input() referenceId: number;
    @Input() patientId: number;
    @Output() updateReference = new EventEmitter<boolean>;

    constructor(
        private dialogService: DialogService<AdministrativeReferenceClosurePopupComponent>,
    ) { }

    ngOnInit(): void {
    }

    openReferenceClosurePopup() {
        const dialogRef = this.dialogService.open(
            AdministrativeReferenceClosurePopupComponent,
            {
                dialogWidth: DialogWidth.SMALL,
                blockCloseClickingOut: true
            },
            {
                referenceId: this.referenceId,
                patientId: this.patientId
            }
        );
        dialogRef.afterClosed().subscribe(() => {
            this.updateReference.emit();
        });
    }

}
