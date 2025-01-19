import { Component, Inject, OnInit } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { CompletePatientDto, ProfessionalDto, VirtualConsultationDto } from '@api-rest/api-model';
import { HealthcareProfessionalByInstitutionService } from '@api-rest/services/healthcare-professional-by-institution.service';
import { PatientService } from '@api-rest/services/patient.service';
import { PatientNameService } from '@core/services/patient-name.service';
import { PatientBasicData } from '@presentation/utils/patient.utils';
import { TypeaheadOption } from '@presentation/components/typeahead/typeahead.component';
import { MapperService } from '@presentation/services/mapper.service';

@Component({
  selector: 'app-transfer-request',
  templateUrl: './transfer-request.component.html',
  styleUrls: ['./transfer-request.component.scss']
})
export class TransferRequestComponent implements OnInit {
  patientBasicData: PatientBasicData;
  responsibleProfessionals: TypeaheadOption<any>[];
  resposibleSelected: ProfessionalDto;
  showMessage: boolean = false;

  constructor(public dialogRef: MatDialogRef<TransferRequestComponent>, @Inject(MAT_DIALOG_DATA) public data: {
    virtualConsultation: VirtualConsultationDto
  }, private patientService: PatientService,
    private mapperService: MapperService,
    private healthcareProfessionalByInstitucion: HealthcareProfessionalByInstitutionService, private namesService: PatientNameService) {

  }

  ngOnInit(): void {
    this.healthcareProfessionalByInstitucion.getVirtualConsultationResponsiblesByInstitutionId().subscribe(professionals => {
      this.responsibleProfessionals = professionals.map(professional => {
        const professionalName = this.namesService.completeName(professional.firstName, professional.nameSelfDetermination, professional.lastName, professional.middleNames, professional.otherLastNames);
        return {
          compareValue: professionalName,
          value: professional.id
        }
      })
    })
    this.patientService.getPatientCompleteData<CompletePatientDto>(this.data.virtualConsultation.patientData.id)
      .subscribe(completeData => {
        this.patientBasicData = this.mapperService.toPatientBasicData(completeData);
      })
  }

  setResponsibleSelectionChange(event) {
    this.resposibleSelected = event;
    this.showMessage = false;
  }

  confirm() {
    if (this.resposibleSelected) {
      this.dialogRef.close(this.resposibleSelected);
    } else {
      this.showMessage = true;
    }
  }
}
