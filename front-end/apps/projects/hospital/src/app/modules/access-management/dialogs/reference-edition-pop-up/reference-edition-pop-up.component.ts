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
import { switchMap, take, tap } from 'rxjs';

@Component({
	selector: 'app-reference-edition-pop-up',
	templateUrl: './reference-edition-pop-up.component.html',
	styleUrls: ['./reference-edition-pop-up.component.scss']
})
export class ReferenceEditionPopUpComponent implements OnInit {

	destinationInstitutionChanged = false;
	submitForm = false;
	newReferenceInfo: ReferenceDto;
	referenceFiles: ReferenceFiles;
	deletedIdsFiles: number[] = [];
	oldReferenceInfo: OldReferenceInformation;

	constructor(
		@Inject(MAT_DIALOG_DATA) public data: ReferenceEditionPopUpData,
		private readonly institutionalReferenceReportService: InstitutionalReferenceReportService,
		private readonly snackBarService: SnackBarService,
		private dialogRef: MatDialogRef<ReferenceEditionPopUpComponent>,
		private readonly referenceFileService: ReferenceFileService,
	) { }

	ngOnInit() {
		this.setOldReferenceInformation();
	}

	save() {
		this.submitForm = true;
		if (!this.referenceFiles?.newFiles.length) {
			if (this.referenceFiles?.oldFiles?.length)
				this.newReferenceInfo = { ...this.newReferenceInfo, fileIds: this.referenceFiles.oldFiles.map(files => files.fileId) };
			
			if (this.data.isGestor) {
				if (this.deletedIdsFiles.length)
				this.referenceFileService.deleteReferenceFiles(this.deletedIdsFiles).pipe(take(1))
				.subscribe(deleted => {
						if (deleted) this.modifyReferenceAsGestor();
					});

				else this.modifyReferenceAsGestor();
			}
			else this.modifyReference();
			return;
		}
		if (this.data.isGestor) {
			if (this.deletedIdsFiles.length)
			this.referenceFileService.deleteReferenceFiles(this.deletedIdsFiles).pipe(take(1))
				.subscribe(deleted => {
					if (deleted) this.loadNewFilesAndModifyReferenceAsGestor();
				});

			else this.loadNewFilesAndModifyReferenceAsGestor();
		}
		else this.loadFilesAndModifyReference();
	}

	setPriorityId(priority: number) {
		this.newReferenceInfo = { ...this.newReferenceInfo, priority };
	}

	setNote(note: string) {
		this.newReferenceInfo = { ...this.newReferenceInfo, note };
	}

	setDestinationInstitutionId(destinationInstitutionId: number) {
		if (this.data.referenceDataDto.institutionDestination.id !== destinationInstitutionId)
			this.destinationInstitutionChanged = true;
		else
			this.destinationInstitutionChanged = false;
		this.submitForm = false;
		this.newReferenceInfo = { ...this.newReferenceInfo, destinationInstitutionId };
	}

	setReferencesFiles(referenceFiles: ReferenceFiles) {
		this.referenceFiles = referenceFiles;
	}

	setDeletesFiles(deletedIdFiles: number[]) {
		this.deletedIdsFiles = deletedIdFiles;
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

	private modifyReferenceAsGestor() {
		if (this.newReferenceInfo.destinationInstitutionId)
			this.institutionalReferenceReportService.modifyReferenceWidhInstitutionAsGestor(this.data.referenceDataDto.id, this.newReferenceInfo.destinationInstitutionId, []).subscribe({
				next: () => this.referenceEditionSuccess(),
				error: () => this.snackBarService.showError("access-management.reference-edition.snack_bar_description.REFERENCE_EDITION_ERROR")
			});

		else
			this.institutionalReferenceReportService.modifyReferenceAsGestor(this.data.referenceDataDto.id, []).subscribe({
				next: () => this.referenceEditionSuccess(),
				error: () => this.snackBarService.showError("access-management.reference-edition.snack_bar_description.REFERENCE_EDITION_ERROR")
			});
	}

	private loadNewFilesAndModifyReferenceAsGestor() {
		let filesIds: number[] = [];
		const patientId = this.data.referencePatientDto.patientId;

		const uploadObservables = this.referenceFileService.uploadReferenceFiles(patientId, this.referenceFiles.newFiles)
			.pipe(
				tap(newFileIds => {
						filesIds = newFileIds;
					}
				)
			);
		if (this.newReferenceInfo.destinationInstitutionId)
			uploadObservables.pipe(
				switchMap(() => {
					if (this.referenceFiles?.oldFiles) filesIds = this.concatOldAndNewFiles(this.referenceFiles.oldFiles, filesIds);
					return this.institutionalReferenceReportService.modifyReferenceWidhInstitutionAsGestor(this.data.referenceDataDto.id, this.newReferenceInfo.destinationInstitutionId, filesIds)
				})
			).subscribe({
				next: () => this.referenceEditionSuccess(),
				error: () => this.referenceEditionError(filesIds)
			});
		
		else 
			uploadObservables.pipe(
				switchMap(() => {
					if (this.referenceFiles?.oldFiles) filesIds = this.concatOldAndNewFiles(this.referenceFiles.oldFiles, filesIds);
					return this.institutionalReferenceReportService.modifyReferenceAsGestor(this.data.referenceDataDto.id, filesIds)
				})
			).subscribe({
				next: () => this.referenceEditionSuccess(),
				error: () => this.referenceEditionError(filesIds)
			});
	}

	private loadFilesAndModifyReference() {
		let filesIds: number[] = [];
		const patientId = this.data.referencePatientDto.patientId;

		const uploadObservables = this.referenceFileService.uploadReferenceFiles(patientId, this.referenceFiles.newFiles)
			.pipe(
				tap(newFileIds => {
						filesIds = newFileIds;
					}
				)
			);

		uploadObservables.pipe(
			switchMap(() => {
				const allReferenceFiles = this.concatOldAndNewFiles(this.referenceFiles.oldFiles, filesIds);
				this.newReferenceInfo = { ...this.newReferenceInfo, fileIds: allReferenceFiles };
				return this.institutionalReferenceReportService.modifyReference(this.data.referenceDataDto.id, this.newReferenceInfo);
			})
		).subscribe({
			next: () => this.referenceEditionSuccess(),
			error: () => this.referenceEditionError(filesIds)
		});
	}

	private concatOldAndNewFiles(oldFiles: ReferenceCounterReferenceFileDto[], newFileIds: number[]): number[] {
		const oldFilesId = oldFiles?.map(files => files.fileId) || [];
		return [...oldFilesId, ...newFileIds].flat();
	}

	private referenceEditionSuccess() {
		this.snackBarService.showSuccess("access-management.reference-edition.snack_bar_description.FILES_SUCCESS");
		this.snackBarService.showSuccess("access-management.reference-edition.snack_bar_description.REFERENCE_EDITION_SUCCESS");
		this.dialogRef.close([true, this.destinationInstitutionChanged]);
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
	isGestor?: boolean,
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
