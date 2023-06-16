import { Component, Inject } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { InternmentEpisodeDocumentService } from '@api-rest/services/internment-episode-document.service';
import { SnackBarService } from '@presentation/services/snack-bar.service';

@Component({
  selector: 'app-delete-document-popup',
  templateUrl: './delete-document-popup.component.html',
  styleUrls: ['./delete-document-popup.component.scss']
})
export class DeleteDocumentPopupComponent {

  constructor(private readonly snackBarService: SnackBarService,
              private internmentEpisodeDocumentService: InternmentEpisodeDocumentService,
              public dialogRef: MatDialogRef<DeleteDocumentPopupComponent>,
              @Inject(MAT_DIALOG_DATA) public data) { }

  delete() {
    this.internmentEpisodeDocumentService.deleteDocument(this.data.episodeDocumentId)
      .subscribe(response => {
        if (response) {
          this.snackBarService.showSuccess('internaciones.internacion-paciente.documents.dialogs.delete.SUCCESS');
          this.dialogRef.close();
        }
      })
  }

}
