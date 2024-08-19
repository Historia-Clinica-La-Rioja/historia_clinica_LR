import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { DocumentHealthcareProfessionalDto, EProfessionType, HCEHealthcareProfessionalDto, ProfessionalDto } from '@api-rest/api-model';
@Component({
  selector: 'app-add-member-medical-team',
  templateUrl: './add-member-medical-team.component.html',
  styleUrls: ['./add-member-medical-team.component.scss']
})
export class AddMemberMedicalTeamComponent {
  professional: AddMemberMedicalTeam ={
    professionalData: {
      healthcareProfessional: {
        id: null,
        licenseNumber: null,
        person: null,
      },
      profession: {
        type: null,
        otherTypeDescription: null,
      }
    },
    descriptionType: null,
  }; 
  readonly otherProfession = EProfessionType.OTHER;
  showErrorProfessionalRepeated = false;
  constructor(public dialogRef: MatDialogRef<AddMemberMedicalTeamComponent>,
    @Inject(MAT_DIALOG_DATA) public data: { professionals: ProfessionalDto[], professions: any[], idProfessionalSelected: number }) {
   }

  selectProfessional(professional: HCEHealthcareProfessionalDto) {
    if(this.data.idProfessionalSelected !== professional?.id){
      this.professional.professionalData.healthcareProfessional = professional;
      this.showErrorProfessionalRepeated = false;
    }else{
      this.showErrorProfessionalRepeated = true;
    }
  }

  emitProfessionalSelected(){
    this.professional.descriptionType = this.data.professions.find(profession => this.professional.professionalData.profession.type === profession.id).description;
    this.dialogRef.close(this.professional? this.professional : null);
  }
}

export interface AddMemberMedicalTeam {
  professionalData: DocumentHealthcareProfessionalDto,
  descriptionType: string
  }