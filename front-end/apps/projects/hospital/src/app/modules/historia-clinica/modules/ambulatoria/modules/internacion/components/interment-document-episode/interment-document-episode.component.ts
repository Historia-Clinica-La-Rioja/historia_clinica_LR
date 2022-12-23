import { Component, EventEmitter, Input, Output } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { saveAs } from 'file-saver';
import { EpisodeDocumentResponseDto } from '@api-rest/api-model';
import { InternmentEpisodeDocumentService } from '@api-rest/services/internment-episode-document.service';
import { AttachDocumentPopupComponent } from '../../dialogs/attach-document-popup/attach-document-popup.component';
import { DeleteDocumentPopupComponent } from '../../dialogs/delete-document-popup/delete-document-popup.component';

@Component({
  selector: 'app-interment-document-episode',
  templateUrl: './interment-document-episode.component.html',
  styleUrls: ['./interment-document-episode.component.scss']
})
export class IntermentDocumentEpisodeComponent {

  @Input() documents: EpisodeDocumentResponseDto[];
  @Input() internmentEpisodeId: number;
  @Output() updateDocuments: EventEmitter<any> = new EventEmitter();

  constructor(public dialog: MatDialog,
              private internmentEpisodeDocumentService: InternmentEpisodeDocumentService) { }

  onFileSelected(event) {
    const file: File = event.target.files[0];

    if (file) this.openAttachDialog(file);
  }

  openAttachDialog(file: File) {
    const dialogRef = this.dialog.open(AttachDocumentPopupComponent, {
			disableClose: true,
			width: '35%',
			data: {
        file,
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
    this.internmentEpisodeDocumentService.download(episodeDocumentId)
      .subscribe(resp => {
        saveAs(resp, fileName);
      });
  }
}