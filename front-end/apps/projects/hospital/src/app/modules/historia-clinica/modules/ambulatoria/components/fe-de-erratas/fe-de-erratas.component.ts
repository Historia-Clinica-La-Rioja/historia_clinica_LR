import { Component, OnInit, Input, Output, EventEmitter, Inject } from '@angular/core';
import { ErrataService } from './service/fe-de-erratas.service';   
import { MAT_DIALOG_DATA } from '@angular/material/dialog';
import { HealthcareProfessionalService } from '@api-rest/services/healthcare-professional.service';
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
    private errataService: ErrataService,
    private healthcareService: HealthcareProfessionalService, 
    @Inject(MAT_DIALOG_DATA) public data: any,

  ) {
    console.log('Data received in FeDeErratasComponent:', data);
    this.institutionId = data.institutionId;
    this.healthcareProfessionalId = data.healthcareProfessionalId;
    this.documentId = data.documentId;
  }

  ngOnInit(): void {
    this.healthcareService.getHealthcareProfessionalByUserId().subscribe(
      (id: number) => {
        this.healthcareProfessionalId = id;  // Asigna el valor obtenido
      },
      (error) => {
        console.error('Error fetching healthcareProfessionalId:', error);
      }
    );
  }

  submitErrata() {
    const errataData = {
      documentId: this.documentId,
      healthcareProfessionalId: this.healthcareProfessionalId,
      institutionId: this.institutionId,
      errataText: this.errataText,  
    };

    this.errataService.createErrata(this.institutionId, errataData).subscribe(
      (response) => {
        console.log('Errata created successfully', response);
      },
      (error) => {
        console.error('Error creating errata:', error);
      }
    );
  }
}
