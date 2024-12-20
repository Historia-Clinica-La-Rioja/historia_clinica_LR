import { NoWhitespaceValidator } from '@core/utils/form.utils';
import { Component, Inject, OnInit } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { ReferenceAdministrativeClosureDto } from '@api-rest/api-model';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { ReferenceAdministrativeClosureService } from '@api-rest/services/reference-administrative-closure.service';
import { ButtonType } from '@presentation/components/button/button.component';
import { SnackBarService } from '@presentation/services/snack-bar.service';
import { CounterreferenceFileService } from '@api-rest/services/counterreference-file.service';
import { take } from 'rxjs';

@Component({
    selector: 'app-administrative-reference-closure-popup',
    templateUrl: './administrative-reference-closure-popup.component.html',
    styleUrls: ['./administrative-reference-closure-popup.component.scss']
})
export class AdministrativeReferenceClosurePopupComponent implements OnInit {

    ButtonType = ButtonType;
    closureForm: FormGroup<FormClosure>
    closureFiles: File[] = [];
    loading: boolean = false;

    constructor(
        @Inject(MAT_DIALOG_DATA) public data: AdministrativeReferenceClosurePopupData,
        private readonly formBuilder: FormBuilder,
        private readonly counterreferenceFileService: CounterreferenceFileService,
        private readonly referenceAdministrativeClosureService: ReferenceAdministrativeClosureService,
        private dialogRef: MatDialogRef<AdministrativeReferenceClosurePopupComponent>,
        private readonly snackBarService: SnackBarService,
    ) { }

    ngOnInit(): void {
        this.closureForm = this.formBuilder.group({
            closureNote: new FormControl(null, { validators: [Validators.required, NoWhitespaceValidator()]}),
            files: new FormControl([])
        })
    }

    addNewFile(newFile: Event) {
        const fileTarget = newFile.target as HTMLInputElement;
        this.closureFiles = this.closureFiles.concat(Array.from(fileTarget.files));
    }

    removeFile(index: number) {
        this.closureFiles.splice(index, 1);
    }

    confirmClosure() {
        if (this.closureForm.valid) {
            this.loading = true;
            let closureData: ReferenceAdministrativeClosureDto;
            let fileIds: number[] = [];
            let filesUploadedLength = 0;

            if (this.closureFiles.length) {
                for (let file of this.closureFiles) {
                    this.counterreferenceFileService.uploadCounterreferenceFiles(this.data.patientId, file).subscribe(
                        newFileId => {
                            fileIds.push(newFileId);
                            filesUploadedLength = filesUploadedLength + 1;
                            
                            if (this.closureFiles.length === filesUploadedLength){
                                closureData = this.setClosureData(fileIds);
                                this.createAdministrativeReferenceClosure(closureData);
                            }
                        },
                        () => {
							this.snackBarService.showError('ambulatoria.paciente.counterreference.messages.ERROR');
							this.counterreferenceFileService.deleteCounterreferenceFiles(fileIds);
						}
                    );
                }
            }

            else {
                closureData = this.setClosureData([]);
                this.createAdministrativeReferenceClosure(closureData);
            }
        }

    }

    createAdministrativeReferenceClosure(closureData: ReferenceAdministrativeClosureDto) {
        this.referenceAdministrativeClosureService.administrativeReferenceCloure(closureData).pipe(take(1))
        .subscribe((success) => {
            this.showSnackBarMessage(success);
            this.dialogRef.close();
        });
    }

    showSnackBarMessage(success: boolean) {
        if (success) this.snackBarService.showSuccess("access-management.reference-edition.snack_bar_description.ADMINISTRATIVE_CLOSURE_SUCCESS");
        else this.snackBarService.showError("access-management.reference-edition.snack_bar_description.ADMINISTRATIVE_CLOSURE_ERROR");
    }

    private setClosureData(newFilesIds: number[]): ReferenceAdministrativeClosureDto {
        return {
            closureNote: this.closureForm.controls.closureNote.value,
            fileIds: newFilesIds,
            referenceId: this.data.referenceId
        }
    }

}

interface FormClosure {
    closureNote: FormControl<string | null>,
    files: FormControl<File[] | null>
}

interface AdministrativeReferenceClosurePopupData {
    referenceId: number,
    patientId: number
}
