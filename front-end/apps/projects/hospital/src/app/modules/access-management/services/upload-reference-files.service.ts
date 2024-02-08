import { Injectable } from '@angular/core';
import { ReferenceFileService } from '@api-rest/services/reference-file.service';
import { SnackBarService } from '@presentation/services/snack-bar.service';
import { Observable, from, mergeMap, tap, catchError, toArray, switchMap, EMPTY } from 'rxjs';

@Injectable()
export class UploadReferenceFilesService {

	constructor(
		private readonly referenceFileService: ReferenceFileService,
		private readonly snackBarService: SnackBarService,
	) { }

	loadReferenceFilesAndGetIds(files: File[], patientId: number): Observable<number[]> {
		let filesId: number[] = [];
		return from(files).pipe(
			mergeMap(fileToUpdate => this.referenceFileService.uploadReferenceFiles(patientId, fileToUpdate)),
			tap(fileId => filesId.push(fileId)),
			catchError(err => this.handleFilesError(filesId)),
			toArray())
	}

	private handleFilesError(filesIdToDelete: number[]): Observable<never> {
		this.snackBarService.showError("access-management.reference-edition.snack_bar_description.FILES_ERROR");
		return this.referenceFileService.deleteReferenceFiles(filesIdToDelete).pipe(
			switchMap(deleteSuccess => {
				if (deleteSuccess)
					return EMPTY;
			})
		)
	}
}
