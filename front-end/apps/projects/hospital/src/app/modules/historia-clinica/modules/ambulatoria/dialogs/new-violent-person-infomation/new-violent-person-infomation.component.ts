import { Component, OnInit } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';
import { EAggressorRelationship, MasterDataDto } from '@api-rest/api-model';
import { AddressMasterDataService } from '@api-rest/services/address-master-data.service';
import { DEFAULT_COUNTRY_ID, hasError, updateControlValidator } from '@core/utils/form.utils';
import { Observable } from 'rxjs';
import { CustomViolenceReportAggressorDto, ViolenceAggressorsNewConsultationService } from '../../services/violence-aggressors-new-consultation.service';
import { MatDialogRef } from '@angular/material/dialog';
import { AggressorRelationship, BasicOptions, CriminalRecordStatus, InstitutionOptions, LiveTogetherStatus, RelationshipLengths, StateOptions, ViolenceFrequencys } from '../../constants/violence-masterdata';

@Component({
  selector: 'app-new-violent-person-infomation',
  templateUrl: './new-violent-person-infomation.component.html',
  styleUrls: ['./new-violent-person-infomation.component.scss']
})

export class NewViolentPersonInfomationComponent implements OnInit {
  basicOptions = BasicOptions;
  stateOptions = StateOptions;
  institutionOptions = InstitutionOptions;
  aggressorRelationship = AggressorRelationship;
  liveTogetherStatus = LiveTogetherStatus;
  relationshipLengths = RelationshipLengths;
  violenceFrequencys = ViolenceFrequencys;
  criminalRecordStatus = CriminalRecordStatus;
  relationAcquaintance = EAggressorRelationship.ACQUAINTANCE;
  hasError = hasError;
  form: FormGroup;
  provinces$: Observable<MasterDataDto[]>;
  departments$: Observable<MasterDataDto[]>;
  cities$: Observable<MasterDataDto[]>;
  constructor(public dialogRef: MatDialogRef<NewViolentPersonInfomationComponent>,
    private addressMasterDataService: AddressMasterDataService,
    private readonly violenceAggressorsNewConsultationService: ViolenceAggressorsNewConsultationService) { }

  ngOnInit(): void {
    this.form = new FormGroup({
      lastname: new FormControl(null, Validators.required),
      name: new FormControl(null, Validators.required),
      age: new FormControl(null, Validators.required),
      address: new FormControl(null, Validators.required),
      addressProvinceId: new FormControl(null, Validators.required),
      addressDepartmentId: new FormControl(null, Validators.required),
      addressCityId: new FormControl(null, Validators.required),
      hasAfirearm: new FormControl(null),
      hasBeenTreated: new FormControl(null),
      belongsToSecurityForces: new FormControl(null),
      inDuty: new FormControl(null),
      securityForceType: new FormControl(null),
      aggressorRelation: new FormControl(null, Validators.required),
      aggressorRelationTextFree: new FormControl(null, Validators.required),
      livesWithVictim: new FormControl(null, Validators.required),
      relationshipLength: new FormControl(null, Validators.required),
      violenceFrequency: new FormControl(null, Validators.required),
      hasPreviousEpisodes: new FormControl(null, Validators.required),
    });
    this.provinces$ = this.addressMasterDataService.getByCountry(DEFAULT_COUNTRY_ID);
  }

  addAggressor() {
    this.form.markAllAsTouched();
    if (this.form.valid) {
      this.violenceAggressorsNewConsultationService.addToList(this.mapAggressor());
      this.dialogRef.close();
    }
  }

  mapAggressor(): CustomViolenceReportAggressorDto {
    return {
      aggressorData: {
        actorPersonalData: {
          firstName: this.form.value.name,
          lastName: this.form.value.lastname,
          address: this.form.value.address,
          age: this.form.value.age,
          municipalityId: this.form.value.addressCityId.id,
        },
        relationshipWithVictim: this.form.value.aggressorRelation,
        otherRelationshipWithVictim: this.form.value.aggressorRelationTextFree,
      },
      hasBeenTreated: this.form.value.hasBeenTreated,
      hasGuns: this.form.value.hasAfirearm,
      hasPreviousEpisodes: this.form.value.hasPreviousEpisodes,
      livesWithVictim: this.form.value.livesWithVictim,
      relationshipLength: this.form.value.relationshipLength,
      securityForceRelatedData: {
        belongsToSecurityForces: this.form.value.belongsToSecurityForces,
        inDuty: this.form.value.inDuty,
        securityForceTypes: this.form.value.securityForceType,
      },
      violenceViolenceFrequency: this.form.value.violenceFrequency,
      descriptionMunicipality: this.form.value.addressCityId.description,
    }
  }

  setDepartments() {
    this.departments$ = this.addressMasterDataService.getDepartmentsByProvince(this.form.value.addressProvinceId);
  }

  setCities() {
    this.cities$ = this.addressMasterDataService.getCitiesByDepartment(this.form.value.addressDepartmentId);
  }

  resetAllLocaltyControls(event: Event) {
    this.resetDepartmentAndCityControl(event);
    this.form.controls.addressProvinceId.reset();
  }

  resetDepartmentAndCityControl(event: Event) {
    event.stopPropagation();
    this.form.controls.addressDepartmentId.reset();
    this.form.controls.addressCityId.reset();
  }

  resetSecurityForceTypesControl(event: Event) {
    this.form.controls.securityForceTypes.reset();
  }

  resetAggressorRelationControl(event: Event) {
    this.form.controls.aggressorRelation.reset();
  }

  changeBelongsToSecurityForces() {
    if (this.form.value.belongsToSecurityForces) {
      updateControlValidator(this.form, 'inDuty', Validators.required);
    } else {
      updateControlValidator(this.form, 'inDuty', []);
    }
  }

  changeInDuty() {
    if (this.form.value.inDuty === true) {
      updateControlValidator(this.form, 'securityForceTypes', Validators.required);
    } else {
      updateControlValidator(this.form, 'securityForceTypes', []);
    }
  }

  changeAggressorRelation() {
    if (this.form.value.aggressorRelation === this.relationAcquaintance) {
      updateControlValidator(this.form, 'aggressorRelationTextFree', Validators.required);
    } else {
      updateControlValidator(this.form, 'aggressorRelationTextFree', []);
    }
  }

}
