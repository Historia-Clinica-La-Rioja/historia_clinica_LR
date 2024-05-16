import { Component, OnInit } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';
import { EAggressorRelationship, MasterDataDto } from '@api-rest/api-model';
import { AddressMasterDataService } from '@api-rest/services/address-master-data.service';
import { DEFAULT_COUNTRY_ID, hasError, updateControlValidator } from '@core/utils/form.utils';
import { Observable } from 'rxjs';
import { CustomViolenceReportAggressorDto, ViolenceAggressorsNewConsultationService } from '../../services/violence-aggressors-new-consultation.service';
import { MatDialogRef } from '@angular/material/dialog';
import { AggressorRelationship, BasicOptions, CriminalRecordStatus, FormOption, InstitutionOptions, LiveTogetherStatus, RelationshipLengths, StateOptions, ViolenceFrequencys } from '../../constants/violence-masterdata';

export const idNoInfo = 99;
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
      addressDepartment: new FormControl(null, Validators.required),
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
				municipality: {
					id: this.form.value.addressDepartment ? this.form.value.addressDepartment.id : null,
					provinceId: null,
					description: null
				},
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
      	descriptionMunicipality: this.form.value.addressDepartment ? this.form.value.addressDepartment.description : FormOption.WITHOUT_DATA,
		canBeDeleted: true
    }
  }

  setDepartments() {
    if (this.form.value.addressProvinceId === idNoInfo) {
      	updateControlValidator(this.form, 'addressDepartment', []);
		this.form.controls.addressDepartment.setValue(null);
    } else {
      	updateControlValidator(this.form, 'addressDepartment', Validators.required);
    }
    this.departments$ = this.addressMasterDataService.getDepartmentsByProvince(this.form.value.addressProvinceId);
  }

  resetAllLocaltyControls(event: Event) {
    this.resetDepartmentControl(event);
    this.form.controls.addressProvinceId.reset();
  }

  resetDepartmentControl(event: Event) {
    event.stopPropagation();
    this.form.controls.addressDepartment.reset();
  }

  resetSecurityForceTypesControl(event: Event) {
    event.stopPropagation();
    this.form.controls.securityForceType.reset();
  }

  resetAggressorRelationControl(event: Event) {
    event.stopPropagation();
    this.form.controls.aggressorRelation.reset();
  }

  changeBelongsToSecurityForces() {
    if (this.form.value.belongsToSecurityForces) {
      updateControlValidator(this.form, 'inDuty', Validators.required);
    } else {
      this.form.controls.inDuty.setValue(null);
	  this.form.controls.securityForceType.setValue(null);
	  updateControlValidator(this.form, 'securityForceType', []);
      updateControlValidator(this.form, 'inDuty', []);
    }
  }

  changeInDuty() {
    if (this.form.value.inDuty === true) {
      updateControlValidator(this.form, 'securityForceType', Validators.required);
    } else {
      updateControlValidator(this.form, 'securityForceType', []);
	  this.form.controls.securityForceType.setValue(null);
    }
  }

  changeAggressorRelation() {
    if (this.form.value.aggressorRelation === this.relationAcquaintance) {
      updateControlValidator(this.form, 'aggressorRelationTextFree', Validators.required);
    } else {
      updateControlValidator(this.form, 'aggressorRelationTextFree', []);
	  this.form.controls.aggressorRelationTextFree.reset();
    }
  }

}
