import { Component, OnInit, ElementRef } from '@angular/core';
import { FormBuilder, Validators, FormGroup, FormArray } from '@angular/forms';
import { Router } from '@angular/router';
import { DatosPersonales} from '../../pacientes.model';
import { APatientDto, BMPatientDto } from '@api-rest/api-model';
import { PatientService } from '@api-rest/services/patient.service';
import { scrollIntoError, hasError } from "@core/utils/form.utils";
@Component({
  selector: 'app-new-patient',
  templateUrl: './new-patient.component.html',
  styleUrls: ['./new-patient.component.scss']
})
export class NewPatientComponent implements OnInit {

  public form: FormGroup;
  public datosPersonales: DatosPersonales = new DatosPersonales();
  public personResponse: BMPatientDto;
  public formSubmitted: boolean = false; 
  public todaysDate: Date = new Date();
  public hasError = hasError;
  private scrollIntoError = scrollIntoError;
  // CAMBIAR LOS TIPOS POR LOS DTOS CORRESPONDIENTES
  public genders:any[];
  public cities:any[];
  public identificationTypeList:any[];
  
  constructor(
    private formBuilder: FormBuilder,
    private router: Router,
    private el: ElementRef,
    private patientService: PatientService,
  ) { }

  ngOnInit(): void {
    this.form = this.formBuilder.group({
      firstName: [null, [Validators.required]],
      middleNames: [],
      lastName: [null, [Validators.required]],
      otherLastNames: [],
      identificationTypeId: [null, [Validators.required]],
      identificationNumber: [],
      genderId: [null, [Validators.required]],
      birthDate: [null, [Validators.required]],
      
      //Person extended
      cuil: [],
      mothersLastName: [],
      phoneNumber: [null, [Validators.required]], 
      email: [null, [Validators.required,Validators.email]],
      ethnic: [],
      religion: [],
      nameSelfDetermination: [],
      genderSelfDeterminationId: [],

      //Address
      addressStreet: [null, [Validators.required]],
      addressNumber: [null, [Validators.required]],
      addressFloor: [null],
      addressApartment: [null],
      addressQuarter:[null],
      addressCityId:  [null, [Validators.required]],
      addressPostcode: [null, [Validators.required]]
    });
    
    this.patientService.getGenders().subscribe(
        genders => { this.genders = genders;}
    );

    this.patientService.getCities().subscribe(
      cities => { this.cities = cities;}
    );

    this.patientService.getIdentitifacionType().subscribe(
      identificationTypes => { this.identificationTypeList = identificationTypes;}
    );
  }

  save():void{
    this.formSubmitted = true;
		if (this.form.valid) {
			let personRequest: APatientDto = mapToPersonRequest(this.datosPersonales);
      this.patientService.addPatient(personRequest)
        .subscribe(person => {
          this.personResponse = person;
          // Ir a la proxima pantalla
        });
		} else {
			scrollIntoError(this.form,this.el);
		}

		function mapToPersonRequest(datosPersonales: DatosPersonales): APatientDto {
			return {
        birthDate:datosPersonales.birthDate,
        firstName: datosPersonales.firstName,
        genderId: datosPersonales.genderId,
        identificationTypeId: datosPersonales.identificationTypeId,
        identificationNumber: datosPersonales.identificationNumber,
        lastName: datosPersonales.lastName,
        middleNames: datosPersonales.middleNames,
        otherLastNames: datosPersonales.otherLastNames, 
        //Person extended
        cuil: datosPersonales.cuil,
        email: datosPersonales.email,
        ethnic: datosPersonales.ethnic,
        genderSelfDeterminationId: datosPersonales.genderSelfDeterminationId,
        mothersLastName: datosPersonales.mothersLastName,
        nameSelfDetermination: datosPersonales.nameSelfDetermination,
        phoneNumber: datosPersonales.phoneNumber, 
        religion: datosPersonales.religion,
        //Address
        apartment:datosPersonales.addressApartment,
        cityId:datosPersonales.addressCityId,
        floor:datosPersonales.addressFloor,
        number:datosPersonales.addressNumber,
        postcode:datosPersonales.addressPostcode,
        quarter:datosPersonales.addressQuarter,
        street:datosPersonales.addressStreet,
        //Mock
        typeId: 1,
        comments: "Sin comentarios",
        identityVerificationStatusId: 1
      };
       
    }
  }
  goBack(): void {
    this.formSubmitted = false;
		this.router.navigate(['/pacientes']);
  }
}