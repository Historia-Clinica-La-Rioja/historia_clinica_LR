import { Component, OnInit } from '@angular/core';
import { SnackBarService } from '@presentation/services/snack-bar.service';

@Component({
  selector: 'app-delete-document-popup',
  templateUrl: './delete-document-popup.component.html',
  styleUrls: ['./delete-document-popup.component.scss']
})
export class DeleteDocumentPopupComponent implements OnInit {

  constructor(private readonly snackBarService: SnackBarService) { }

  ngOnInit(): void {
  }

  delete(value) {
    this.snackBarService.showError('internaciones.internacion-paciente.documents.dialogs.delete.SUCCESS');
  }

}
