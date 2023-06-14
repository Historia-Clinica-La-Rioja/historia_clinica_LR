import { Component, OnInit, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialog, MatDialogRef } from '@angular/material/dialog';
import { StudyStatusPopupComponent } from '../study-status-popup/study-status-popup.component';
import { WorklistService } from '@api-rest/services/worklist.service';
import { InstitutionBasicInfoDto } from '@api-rest/api-model';
import { ContextService } from '@core/services/context.service';
import { UntypedFormBuilder, UntypedFormGroup, Validators } from '@angular/forms';
import { AppointmentsService } from '@api-rest/services/appointments.service';

@Component({
    selector: 'app-derive-report',
    templateUrl: './derive-report.component.html',
    styleUrls: ['./derive-report.component.scss']
})
export class DeriveReportComponent implements OnInit {

    public form: UntypedFormGroup;
    informerInstitutions: InstitutionBasicInfoDto[] = [];
    isSubmited = false;

    constructor(
        @Inject(MAT_DIALOG_DATA) public readonly data: {
            appointmentId: number
        },
        public dialogRef: MatDialogRef<DeriveReportComponent>,
        public dialog: MatDialog,
        private readonly worklistService: WorklistService,
        private readonly contextService: ContextService,
        private readonly appointmentsService: AppointmentsService,
        private formBuilder: UntypedFormBuilder) { }

    ngOnInit(): void {
        this.worklistService.getInformerInstitutions().subscribe(institutions => {
            this.informerInstitutions = institutions.filter(i => i.id !== this.contextService.institutionId);
        })
        this.form = this.formBuilder.group({
            informerInstitution: [null, Validators.required]
        });
    }

    closeDialog(destinationInstitution?: InstitutionBasicInfoDto) {
        this.dialogRef.close(destinationInstitution ? destinationInstitution : null)
    }

    deriveReport() {
        this.isSubmited = true;
        if (this.form.valid) {
            let destinationInstitution = this.form.controls.informerInstitution.value.id;
            this.appointmentsService.deriveReport(this.data.appointmentId, destinationInstitution).subscribe(() => 
                this.openDeriveStatusPopUp()
            );
        }
    }

    openDeriveStatusPopUp() {
        this.closeDialog(this.form.controls.informerInstitution.value)
        const dialogRef = this.dialog.open(StudyStatusPopupComponent, {
            width: '30%',
            autoFocus: false,
            data: {
                icon: 'subdirectory_arrow_right',
                iconColor: 'lightgrey',
                popUpMessage: this.form.controls.informerInstitution.value.name,
                popUpMessageTranslate: 'image-network.worklist.REPORT_REFERRED',
                acceptBtn: true,
                iconCircle: true
            }
        });
        dialogRef.afterClosed().subscribe();
    }
}
