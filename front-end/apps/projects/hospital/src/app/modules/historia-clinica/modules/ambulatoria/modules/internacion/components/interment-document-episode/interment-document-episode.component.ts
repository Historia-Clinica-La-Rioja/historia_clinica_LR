import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { EpisodeDocumentResponseDto } from '@api-rest/api-model';
import { InternmentEpisodeDocumentService } from '@api-rest/services/internment-episode-document.service';
import { AttachDocumentPopupComponent } from '../../dialogs/attach-document-popup/attach-document-popup.component';
import { DeleteDocumentPopupComponent } from '../../dialogs/delete-document-popup/delete-document-popup.component';
import { InternmentEpisodeService } from '@api-rest/services/internment-episode.service';
import { take } from 'rxjs';

@Component({
	selector: 'app-interment-document-episode',
	templateUrl: './interment-document-episode.component.html',
	styleUrls: ['./interment-document-episode.component.scss']
})
export class IntermentDocumentEpisodeComponent implements OnInit {

	@Input() documents: EpisodeDocumentResponseDto[];
	@Input() internmentEpisodeId: number;
	@Input() disabled: boolean;
	@Output() updateDocuments: EventEmitter<any> = new EventEmitter();
	hasAdministrativeDischarge = true;

	constructor(
		public dialog: MatDialog,
		private internmentEpisodeDocumentService: InternmentEpisodeDocumentService,
		private readonly internmentService: InternmentEpisodeService,
	) { }

	ngOnInit() {
		this.checkForAdministrativeDischarge();
	}

	openAttachDialog() {
		const dialogRef = this.dialog.open(AttachDocumentPopupComponent, {
			disableClose: true,
			width: '40%',
			data: {
				internmentEpisodeId: this.internmentEpisodeId
			}
		});
		dialogRef.afterClosed().subscribe(_ => this.updateDocuments.emit());
	}

	openDeleteDialog(episodeDocumentId: number) {
		const dialogRef = this.dialog.open(DeleteDocumentPopupComponent, {
			disableClose: true,
			width: '35%',
			data: {
				episodeDocumentId,
			}
		});
		dialogRef.afterClosed().subscribe(_ => this.updateDocuments.emit());
	}

	download(episodeDocumentId: number, fileName: string) {
		this.internmentEpisodeDocumentService.download(episodeDocumentId, fileName);
	}

	private checkForAdministrativeDischarge(){
		this.internmentService.getPatientDischarge(this.internmentEpisodeId).pipe(take(1)).subscribe(patientDischarge => {
			this.hasAdministrativeDischarge = !!patientDischarge.administrativeDischargeDate;
		})
	}
}
