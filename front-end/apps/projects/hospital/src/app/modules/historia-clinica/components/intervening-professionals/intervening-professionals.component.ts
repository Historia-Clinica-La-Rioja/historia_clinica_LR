import { Component, EventEmitter, OnInit, Output } from '@angular/core';
import { ELicenseNumberType, ElectronicJointSignatureInstitutionProfessionalDto } from '@api-rest/api-model';
import { ElectronicJointSignatureInstitutionalProfessionalLicenseService } from '@api-rest/services/electronic-joint-signature-institutional-professional-license.service';
import { Professional, ProfessionalService } from '@historia-clinica/services/professionals.service';
import { TypeaheadOption } from '@presentation/components/typeahead/typeahead.component';

@Component({
  selector: 'app-intervening-professionals',
  templateUrl: './intervening-professionals.component.html',
  styleUrls: ['./intervening-professionals.component.scss']
})
export class InterveningProfessionalsComponent implements OnInit {
  @Output() interveningProfessional = new EventEmitter<number[]>();
  professionalsTypeaheadOption: TypeaheadOption<ElectronicJointSignatureInstitutionProfessionalDto>[];
  professionals: ElectronicJointSignatureInstitutionProfessionalDto[];
  initValueProfessional: TypeaheadOption<ElectronicJointSignatureInstitutionProfessionalDto>;
  professionalsService: ProfessionalService
  constructor(private readonly electronicJointSignatureInstitutionalProfessionalLicenseService: ElectronicJointSignatureInstitutionalProfessionalLicenseService) {
    this.professionalsService = new ProfessionalService();
  }

  ngOnInit(): void {
    this.professionalsService.professionals$.subscribe(professionals =>{
      this.interveningProfessional.emit(professionals.map(professional => professional.healthcareProfessionalId));
    });
    this.electronicJointSignatureInstitutionalProfessionalLicenseService.getInstitutionalProfessionalsLicense().subscribe(professionals => {
      this.professionals = professionals;
      this.professionalsTypeaheadOption = this.professionals.map(d => this.toProfessionalTypeahead(d));
    })
  }

  setInterveningProfessional(healthcareProfessionalId: number) {
    const init = {
      compareValue: null,
      value: null,
    }
    if (healthcareProfessionalId) {
      this.professionalsService.add(this.toProfessional(this.professionals.find(p => p.healthcareProfessionalId === healthcareProfessionalId)));
      this.initValueProfessional = init;
    }
  }

  private toProfessional(professionalDto: ElectronicJointSignatureInstitutionProfessionalDto): Professional {
    const moreSpecialities = professionalDto.clinicalSpecialties.length >= 2 ? ' (+' + (professionalDto.clinicalSpecialties.length - 1) + ' más)' : "";
	const license = professionalDto.license.type ? (professionalDto.license.type === ELicenseNumberType.NATIONAL ? 'MN N°'+ professionalDto.license.number : 'MP N°' + professionalDto.license.number) : '';
    return {
      completeName: professionalDto.completeName,
      healthcareProfessionalId: professionalDto.healthcareProfessionalId,
      speciality: professionalDto.clinicalSpecialties[0] + moreSpecialities ,
      license,
    };
  }

  private toProfessionalTypeahead(professionalDto: ElectronicJointSignatureInstitutionProfessionalDto): TypeaheadOption<any> {
    const licence = professionalDto.license.type ? ((professionalDto.license.type === ELicenseNumberType.NATIONAL ? ', MN' : ', MP') + ' ' + professionalDto.license.number) : '';
    const moreSpecialities = professionalDto.clinicalSpecialties.length >= 2 ? ' (+' + (professionalDto.clinicalSpecialties.length - 1) + ' más)' : "";
    return {
      compareValue: professionalDto.completeName + licence + ' ' + professionalDto.clinicalSpecialties[0] + moreSpecialities,
      value: professionalDto.healthcareProfessionalId,
    };
  }
}
