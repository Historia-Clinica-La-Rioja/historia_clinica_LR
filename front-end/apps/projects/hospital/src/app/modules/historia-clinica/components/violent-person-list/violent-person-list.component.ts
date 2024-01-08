import { Component } from '@angular/core';
import { EAggressorRelationship, ECriminalRecordStatus, ELiveTogetherStatus, ERelationshipLength, ESecurityForceType, EViolenceFrequency, ViolenceReportAggressorDto } from '@api-rest/api-model';
import { AggressorRelationship, CriminalRecordStatus, FormOption, InstitutionOptions, LiveTogetherStatus, RelationshipLengths, ViolenceFrequencys } from '@historia-clinica/modules/ambulatoria/constants/violence-masterdata';
import { CustomViolenceReportAggressorDto, ViolenceAggressorsNewConsultationService } from '@historia-clinica/modules/ambulatoria/services/violence-aggressors-new-consultation.service';

@Component({
  selector: 'app-violent-person-list',
  templateUrl: './violent-person-list.component.html',
  styleUrls: ['./violent-person-list.component.scss']
})
export class ViolentPersonListComponent {
  aggressorsList: ViolenceReportAggressorDto[];

  constructor(private readonly violenceAggressorsNewConsultationService: ViolenceAggressorsNewConsultationService) {
    this.setAggressors();
  }

  removeAggressor(index: number) {
    this.violenceAggressorsNewConsultationService.removeAggressor(index);
  }

  setAggressors() {
    this.violenceAggressorsNewConsultationService.violenceAggressors$.subscribe((concepts: CustomViolenceReportAggressorDto[]) => this.aggressorsList = concepts);
  }

  getDescriptionBasicOptions(value: any): string {
    switch (value) {
      case true:
        return FormOption.YES;
      case false:
        return FormOption.NO;
      case null:
        return FormOption.WITHOUT_DATA;
    }
  }

  getDescriptionSecurityForceType(value: ESecurityForceType): string {
    return InstitutionOptions.find(type => type.value === value).text;
  }

  getDescriptionAggressorRelationship(value: EAggressorRelationship): string {
    return AggressorRelationship.find(relation => relation.value === value).text;
  }

  getDescriptionLivesWithVictim(value: ELiveTogetherStatus): string {
    return LiveTogetherStatus.find(state => state.value === value).text;
  }

  getDescriptionRelationshipLength(value: ERelationshipLength): string {
    return RelationshipLengths.find(length => length.value === value).text;
  }

  getDescriptionViolenceFrequency(value: EViolenceFrequency): string {
    return ViolenceFrequencys.find(frequency => frequency.value === value).text;
  }

  getDescriptionHasPreviousEpisodes(value: ECriminalRecordStatus): string {
    return CriminalRecordStatus.find(record => record.value === value).text;
  }

}
