import { Component, Input, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { DateDto } from '@api-rest/api-model';
import { AttachDocumentPopupComponent } from '../../dialogs/attach-document-popup/attach-document-popup.component';

@Component({
  selector: 'app-interment-document-episode',
  templateUrl: './interment-document-episode.component.html',
  styleUrls: ['./interment-document-episode.component.scss']
})
export class IntermentDocumentEpisodeComponent implements OnInit {

  @Input() documents: Document;

  constructor(public dialog: MatDialog) { }

  ngOnInit(): void {
  }

  onFileSelected(event) {
    const file:File = event.target.files[0];

    if (file) 
      this.openAttachDialog(file.name);
    
  }

  openAttachDialog(fileName: string) {
    const dialogRef = this.dialog.open(AttachDocumentPopupComponent, {
			disableClose: true,
			width: '35%',
			data: {
        fileName
			}
		});
  }
}

export interface Document {
	type: string;
	fileName: string;
	date: DateDto;
}