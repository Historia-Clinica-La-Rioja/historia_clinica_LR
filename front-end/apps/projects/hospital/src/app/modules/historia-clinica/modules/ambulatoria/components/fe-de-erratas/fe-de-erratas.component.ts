import { Component, OnInit, Input, Output, EventEmitter, Inject } from '@angular/core';
import { ErrataService } from './service/fe-de-erratas.service';  // Asegúrate de que la ruta al servicio es correcta
import { MAT_DIALOG_DATA } from '@angular/material/dialog';

@Component({
  selector: 'app-fe-de-erratas',
  templateUrl: './fe-de-erratas.component.html',
  styleUrls: ['./fe-de-erratas.component.scss']
})
export class FeDeErratasComponent implements OnInit {

  @Input() isEditing: boolean = false;
  @Input() documentId: number;
  @Input() institutionId: number;
  @Input() healthcareProfessionalId: number;
  @Output() closeErrata = new EventEmitter<void>();

  @Input() errataText: string;
  currentDocumentId: any;

  constructor(
    private errataService: ErrataService, // Inyectamos el servicio correctamente
    @Inject(MAT_DIALOG_DATA) public data: any,
  ) {
    console.log('Data received in FeDeErratasComponent:', data);
    this.institutionId = data.institutionId;
    this.healthcareProfessionalId = data.healthcareProfessionalId;
    this.documentId = data.documentId;
  }

  ngOnInit(): void {}

  submitErrata() {
    // Crea el objeto errataData desde this.data
    const errataData = {
      documentId: this.documentId,
      healthcareProfessionalId: this.healthcareProfessionalId,
      institutionId: this.institutionId,
      errataText: this.errataText, // Este sería el texto de la errata
    };

    // Llamada al servicio para crear la errata
    this.errataService.createErrata(this.institutionId, errataData).subscribe(
      (response) => {
        console.log('Errata created successfully', response);
        // Aquí puedes agregar lógica para notificar al usuario del éxito
      },
      (error) => {
        console.error('Error creating errata:', error);
        // Notificar al usuario del error
      }
    );
  }
}
