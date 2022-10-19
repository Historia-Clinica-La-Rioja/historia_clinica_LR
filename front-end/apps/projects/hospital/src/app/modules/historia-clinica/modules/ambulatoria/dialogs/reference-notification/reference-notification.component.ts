import { Component, Inject, OnInit } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { ReferenceDto, ReferenceCounterReferenceFileDto } from '@api-rest/api-model';
import { ReferenceFileService } from '@api-rest/services/reference-file.service';
import { PatientNameService } from '@core/services/patient-name.service';
import { Color } from '@presentation/colored-label/colored-label.component';

@Component({
	selector: 'app-reference-notification',
	templateUrl: './reference-notification.component.html',
	styleUrls: ['./reference-notification.component.scss']
})
export class ReferenceNotificationComponent implements OnInit {

	Color = Color;

	constructor(
		@Inject(MAT_DIALOG_DATA) public data: ReferenceDto[],
		private readonly dialogRef: MatDialogRef<ReferenceNotificationComponent>,
		private readonly referenceFileService: ReferenceFileService,
		private readonly patientNameService: PatientNameService,
	) { }

	ngOnInit(): void {

	}

	goToNewConsultation(): void {
		this.dialogRef.close(false);
	}

	goBack(): void {
		this.dialogRef.close(null);
	}

	goToCounterreference(reference): void {
		const counterreference = { isACountisACounterrefer: true, reference: reference }
		this.dialogRef.close(counterreference);
	}

	downloadReferenceFile(file: ReferenceCounterReferenceFileDto) {
		this.referenceFileService.downloadReferenceFiles(file.fileId, file.fileName);
	}

	getFullName(firstName: string, nameSelfDetermination: string): string {
		return `${this.patientNameService.getPatientName(firstName, nameSelfDetermination)}`;
	}
}
