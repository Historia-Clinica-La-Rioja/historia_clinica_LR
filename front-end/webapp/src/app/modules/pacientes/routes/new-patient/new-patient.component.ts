import { Component, OnInit, ElementRef } from '@angular/core';
import { FormBuilder, Validators, FormGroup, FormArray, FormControl } from '@angular/forms';
import { Router, ActivatedRoute } from '@angular/router';
import { APatientDto, BMPatientDto } from '@api-rest/api-model';
import { PatientService } from '@api-rest/services/patient.service';
import { scrollIntoError, hasError } from "@core/utils/form.utils";

const VALID_PATIENT = 2;
@Component({
  selector: 'app-new-patient',
  templateUrl: './new-patient.component.html',
  styleUrls: ['./new-patient.component.scss']
})
export class NewPatientComponent implements OnInit {

  public form: FormGroup;
  public personResponse: BMPatientDto;
  public formSubmitted: boolean = false; 
  public todaysDate: Date = new Date();
  public hasError = hasError;
  // CAMBIAR LOS TIPOS POR LOS DTOS CORRESPONDIENTES
  public genders:any[];
  public cities:any[];
  public identificationTypeList:any[];
  
  constructor(
    private formBuilder: FormBuilder,
    private router: Router,
    private el: ElementRef,
    private patientService: PatientService,
    private route: ActivatedRoute
  ) { }

  ngOnInit(): void {
    
    this.route.queryParams
      .subscribe(params => {
        this.form = this.formBuilder.group({
          firstName: [params.firstName, [Validators.required]],
          middleNames: [params.middleNames],
          lastName: [params.lastName, [Validators.required]],
          otherLastNames: [params.otherLastNames],
          identificationNumber: [params.identificationNumber, [Validators.required]],
          birthDate: [new Date(params.birthDate), [Validators.required]],
          
          //Person extended
          cuil: [],
          mothersLastName: [],
          phoneNumber: [], 
          email: [null,Validators.email],
          ethnic: [],
          religion: [],
          nameSelfDetermination: [],
          genderSelfDeterminationId: [],
    
          //Address
          addressStreet: [],
          addressNumber: [],
          addressFloor: [],
          addressApartment: [],
          addressQuarter:[],
          addressCityId:  [],
          addressPostcode: []
        });

        this.patientService.getGenders().subscribe(
          genders => {
            this.genders = genders;
            this.form.addControl('genderId',new FormControl(params.genderId, Validators.required));
        });
            
        this.patientService.getIdentitifacionType()
          .subscribe( identificationTypes => {
             this.identificationTypeList = identificationTypes;
             this.form.addControl('identificationTypeId',new FormControl(params.identificationTypeId, Validators.required));
          });

      });

    this.patientService.getCities().subscribe(
      cities => { this.cities = cities;}
    );
  }

  save():void{
    this.formSubmitted = true;
		if (this.form.valid) {
			let personRequest: APatientDto = this.mapToPersonRequest();
      this.patientService.addPatient(personRequest)
        .subscribe(person => {
          this.personResponse = person;
          // Ir a la proxima pantalla
        });
		} else {
			scrollIntoError(this.form,this.el);
		}
  }

  private mapToPersonRequest(): APatientDto {
    return {
      birthDate: this.form.controls.birthDate.value,
      firstName: this.form.controls.firstName.value,
      genderId: this.form.controls.genderId.value,
      identificationTypeId: this.form.controls.identificationTypeId.value,
      identificationNumber: this.form.controls.identificationNumber.value,
      lastName: this.form.controls.lastName.value,
      middleNames: this.form.controls.middleNames.value && this.form.controls.middleNames.value.length ? this.form.controls.middleNames.value : null,
      otherLastNames: this.form.controls.otherLastNames.value && this.form.controls.otherLastNames.value.length ? this.form.controls.otherLastNames.value : null,
      //Person extended
      cuil: this.form.controls.cuil.value,
      email:this.form.controls.email.value,
      ethnic: this.form.controls.ethnic.value,
      genderSelfDeterminationId:this.form.controls.genderSelfDeterminationId.value,
      mothersLastName:this.form.controls.mothersLastName.value,
      nameSelfDetermination: this.form.controls.nameSelfDetermination.value,
      phoneNumber: this.form.controls.phoneNumber.value,
      religion: this.form.controls.religion.value,
      //Address
      apartment:this.form.controls.addressApartment.value,
      cityId:this.form.controls.addressCityId.value,
      floor:this.form.controls.addressFloor.value,
      number:this.form.controls.addressNumber.value,
      postcode:this.form.controls.addressPostcode.value,
      quarter:this.form.controls.addressQuarter.value,
      street:this.form.controls.addressStreet.value,
      //Patient
      typeId: VALID_PATIENT,
      comments: null,
      identityVerificationStatusId: null
    };
     
  }

  goBack(): void {
    this.formSubmitted = false;
		this.router.navigate(['/pacientes']);
  }
}