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
    this.emitInterveningProfessional();
  }

  private emitInterveningProfessional() {
    this.interveningProfessional.emit(this.professionalsService.getProfessionals().map(professional => professional.healthcareProfessionalId));
  }

  private toProfessional(professionalDto: ElectronicJointSignatureInstitutionProfessionalDto): Professional {
    const moreSpecialities = professionalDto.licenses.length >= 2 ? ' (+' + (professionalDto.licenses.length - 1) + ' más)' : "";
    return {
      completeName: professionalDto.completeName,
      healthcareProfessionalId: professionalDto.healthcareProfessionalId,
      speciality: professionalDto.licenses[0].clinicalSpecialtyName + moreSpecialities,
      license: professionalDto.licenses[0].type === ELicenseNumberType.NATIONAL ? 'MN N°' + professionalDto.licenses[0].number : 'MP N°' + professionalDto.licenses[0].number,
    };
  }

  private toProfessionalTypeahead(professionalDto: ElectronicJointSignatureInstitutionProfessionalDto): TypeaheadOption<any> {
    const licence = professionalDto.licenses[0].type === ELicenseNumberType.NATIONAL ? ', MN' : ', MP';
    const moreSpecialities = professionalDto.licenses.length >= 2 ? ' (+' + (professionalDto.licenses.length - 1) + ' más)' : "";
    return {
      compareValue: professionalDto.completeName + licence + ' ' + professionalDto.licenses[0].number + ' ' + professionalDto.licenses[0].clinicalSpecialtyName + moreSpecialities,
      value: professionalDto.healthcareProfessionalId,
    };
  }
}
