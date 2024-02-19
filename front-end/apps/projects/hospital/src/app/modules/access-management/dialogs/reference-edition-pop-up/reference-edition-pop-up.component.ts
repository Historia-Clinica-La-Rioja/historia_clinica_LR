import { ReferenceMedicalConceptsInformation } from '@access-management/components/reference-medical-concepts-information/reference-medical-concepts-information.component';
import { HomeInstitutionInformation } from '@access-management/components/home-institution-information/home-institution-information.component';
import { PatientPhone } from '@access-management/components/patient-phone/patient-phone.component';
import { DestinationInstitutionInformation } from '@access-management/components/destination-institution-information/destination-institution-information.component';
import { ReferenceFiles } from '@access-management/components/reference-files/reference-files.component';
import { toOldReferenceInformation } from '@access-management/utils/reference-edition.utils';
import { Component, Inject, OnInit } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { ReferenceCounterReferenceFileDto, ReferenceDataDto, ReferenceDto, ReferencePatientDto } from '@api-rest/api-model';
import { InstitutionalReferenceReportService } from '@api-rest/services/institutional-reference-report.service';
import { ReferenceFileService } from '@api-rest/services/reference-file.service';
import { SnackBarService } from '@presentation/services/snack-bar.service';
import { switchMap } from 'rxjs';
import { UploadReferenceFilesService } from '@access-management/services/upload-reference-files.service';

@Component({
	selector: 'app-reference-edition-pop-up',
	templateUrl: './reference-edition-pop-up.component.html',
	styleUrls: ['./reference-edition-pop-up.component.scss'],
	providers: [UploadReferenceFilesService]
})
export class ReferenceEditionPopUpComponent implements OnInit {

	submitForm = false;
	newReferenceInfo: ReferenceDto;
	referenceFiles: ReferenceFiles;
	oldReferenceInfo: OldReferenceInformation;

	constructor(
		@Inject(MAT_DIALOG_DATA) public data: ReferenceEditionPopUpData,
		private readonly institutionalReferenceReportService: InstitutionalReferenceReportService,
		private readonly snackBarService: SnackBarService,
		private dialogRef: MatDialogRef<ReferenceEditionPopUpComponent>,
		private readonly referenceFileService: ReferenceFileService,
		private readonly uploadReferenceFilesService: UploadReferenceFilesService
	) { }

	ngOnInit() {
		this.setOldReferenceInformation();
	}

	save() {
		this.submitForm = true;
		if (!this.newReferenceInfo.destinationInstitutionId)
			return;

		if (!this.referenceFiles?.newFiles.length) {
			if (this.referenceFiles?.oldFiles?.length)
				this.newReferenceInfo = { ...this.newReferenceInfo, fileIds: this.referenceFiles.oldFiles.map(files => files.fileId) };
			this.modifyReference();
			return;
		}

		this.loadFilesAndModifyReference();
	}

	setPriorityId(priority: number) {
		this.newReferenceInfo = { ...this.newReferenceInfo, priority };
	}

	setNote(note: string) {
		this.newReferenceInfo = { ...this.newReferenceInfo, note };
	}

	setDestinationInstitutionId(destinationInstitutionId: number) {
		this.submitForm = false;
		this.newReferenceInfo = { ...this.newReferenceInfo, destinationInstitutionId };
	}

	setReferencesFiles(referenceFiles: ReferenceFiles) {
		this.referenceFiles = referenceFiles;
	}

	private setOldReferenceInformation() {
		this.oldReferenceInfo = toOldReferenceInformation(this.data.referenceDataDto, this.data.referencePatientDto);
	}

	private modifyReference() {
		const referenceId = this.data.referenceDataDto.id;
		this.institutionalReferenceReportService.modifyReference(referenceId, this.newReferenceInfo).subscribe({
			next: () => this.referenceEditionSuccess(),
			error: () => this.snackBarService.showError("access-management.reference-edition.snack_bar_description.REFERENCE_EDITION_ERROR")
		});
	}

	private loadFilesAndModifyReference() {
		let filesIds: number[];
		this.uploadReferenceFilesService.loadReferenceFilesAndGetIds(this.referenceFiles.newFiles, this.data.referencePatientDto.patientId)
		.pipe(switchMap(newFileIds => {
			filesIds = newFileIds
			const allReferenceFiles = this.concatOldAndNewFiles(this.referenceFiles.oldFiles, newFileIds);
			this.newReferenceInfo = { ...this.newReferenceInfo, fileIds: allReferenceFiles };
			return this.institutionalReferenceReportService.modifyReference(this.data.referenceDataDto.id, this.newReferenceInfo);
		}))
		.subscribe({
			next: () => this.referenceEditionSuccess(),
			error: () => this.referenceEditionError(filesIds)
		});

	}	

	private concatOldAndNewFiles(oldFiles: ReferenceCounterReferenceFileDto[], newFileIds: number[]): number[] {
		const oldFilesId = oldFiles?.map(files => files.fileId) || [];
		return [...oldFilesId, ...newFileIds];
	}

	private referenceEditionSuccess() {
		this.snackBarService.showSuccess("access-management.reference-edition.snack_bar_description.FILES_SUCCESS");
		this.snackBarService.showSuccess("access-management.reference-edition.snack_bar_description.REFERENCE_EDITION_SUCCESS");
		this.dialogRef.close(true);
	}

	private referenceEditionError(filesId: number[]) {
		this.snackBarService.showError("access-management.reference-edition.snack_bar_description.REFERENCE_EDITION_ERROR");
		if (filesId.length)
			this.referenceFileService.deleteReferenceFiles(filesId).subscribe({
				next: () => this.snackBarService.showError("access-management.reference-edition.snack_bar_description.FILES_DELETE_SUCCESS"),
				error: () => this.snackBarService.showError("access-management.reference-edition.snack_bar_description.FILES_DELETE_ERROR")
			});
	}

}

export interface ReferenceEditionPopUpData {
	referenceDataDto: ReferenceDataDto,
	referencePatientDto: ReferencePatientDto,
}

export interface OldReferenceInformation {
	homeInstitutionInfo: HomeInstitutionInformation;
	problems: string[];
	priorityId: number;
	patientPhone: PatientPhone;
	referenceMedicalConceptsInfo: ReferenceMedicalConceptsInformation;
	destinationInstitutionInfo: DestinationInstitutionInformation;
	note: string;
	files: ReferenceCounterReferenceFileDto[];
}
