import { Component, EventEmitter, Input, Output } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { DateDto, EpisodeDocumentResponseDto } from '@api-rest/api-model';
import { AttachDocumentPopupComponent } from '../../dialogs/attach-document-popup/attach-document-popup.component';
import { DeleteDocumentPopupComponent } from '../../dialogs/delete-document-popup/delete-document-popup.component';

@Component({
  selector: 'app-interment-document-episode',
  templateUrl: './interment-document-episode.component.html',
  styleUrls: ['./interment-document-episode.component.scss']
})
export class IntermentDocumentEpisodeComponent {

  @Input() documents: EpisodeDocumentResponseDto;
  @Input() internmentEpisodeId: number;
  @Output() newDocument: EventEmitter<any> = new EventEmitter();

  constructor(public dialog: MatDialog) { }

  onFileSelected(event) {
    const file:File = event.target.files[0];

    if (file) 
      this.openAttachDialog(file);
    
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
    dialogRef.afterClosed().subscribe(_ => this.newDocument.emit());
  }

  openDeleteDialog(event) {
    const dialogRef = this.dialog.open(DeleteDocumentPopupComponent, {
			disableClose: true,
			width: '35%',
			data: {}
		});
  }
}