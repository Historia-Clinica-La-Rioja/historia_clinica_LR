import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { AbstractControl, UntypedFormGroup } from '@angular/forms';
import { MatDialog } from '@angular/material/dialog';
import { BMPersonDto, BasicPatientDto, PatientMedicalCoverageDto } from '@api-rest/api-model';
import { PatientService } from '@api-rest/services/patient.service';
import { PatientMedicalCoverageService } from '@api-rest/services/patient-medical-coverage.service';
import { AddressMasterDataService } from '@api-rest/services/address-master-data.service';
import { PersonService } from '@api-rest/services/person.service';
import { PatientNameService } from '@core/services/patient-name.service';
import { MapperService } from '@core/services/mapper.service';
import {hasError} from '@core/utils/form.utils';
import { MedicalCoverageComponent, PatientMedicalCoverage } from '@pacientes/dialogs/medical-coverage/medical-coverage.component';
import { SnackBarService } from '@presentation/services/snack-bar.service';
import { NewPrescriptionData } from '../../dialogs/nueva-prescripcion/nueva-prescripcion.component';
import { PatientSummary } from 'projects/hospital/src/app/modules/hsi-components/patient-summary/patient-summary.component';
import { Observable, map } from 'rxjs';

const ARGENTINA_ID: number = 14;

@Component({
    selector: 'app-patient-information',
    templateUrl: './patient-information.component.html',
    styleUrls: ['./patient-information.component.scss']
})

export class PatientInformationComponent implements OnInit {

    @Input() data: NewPrescriptionData;
    @Input() prescriptionForm: UntypedFormGroup;
    @Input() isHabilitarRecetaDigitalEnabled: boolean;

	@Output() personEmmiter = new EventEmitter<BMPersonDto>();
	@Output() clearControls = new EventEmitter<AbstractControl>();

    patientData: BasicPatientDto;
    patientSummary: PatientSummary;
    person: BMPersonDto;
    showCoverageMessage: boolean = false;
    patientMedicalCoverages: PatientMedicalCoverage[];

    provinces$: Observable<any[]>;
	departments$: Observable<any[]>;
	countries$: Observable<any[]>;
	cities$: Observable<any[]>;

    hasError = hasError;


    constructor(
        private readonly patientNameService: PatientNameService,
        private readonly patientService: PatientService,
        private readonly patientMedicalCoverageService: PatientMedicalCoverageService,
		private readonly personService: PersonService,
        private readonly addressMasterDataService: AddressMasterDataService,
        private readonly dialog: MatDialog,
        private readonly mapperService: MapperService,
        private readonly snackBarService: SnackBarService,
    ) { }

    ngOnInit(): void {
        this.setMedicalCoverages();
        this.patientService.getPatientBasicData(Number(this.data.patientId)).subscribe((basicData: BasicPatientDto) => {
			this.patientData = basicData;
			this.patientSummary = {
				fullName: this.patientNameService.completeName(this.patientData.person.firstName, this.patientData.person.nameSelfDetermination, this.patientData.person.lastName, this.patientData.person.middleNames, this.patientData.person.otherLastNames),
				id: this.data.patientId,
				identification: {
					number: Number(this.patientData.person.identificationNumber),
					type: this.patientData.person.identificationType
				}
			};
		});
        if (this.isHabilitarRecetaDigitalEnabled)
			this.setCountries();
    }

    selectPatientMedicalCoverage(patientMedicalCoverage: PatientMedicalCoverage) {
		(! patientMedicalCoverage.affiliateNumber && patientMedicalCoverage.medicalCoverage.type != 3)
			? this.showCoverageMessage = true
			: this.showCoverageMessage = false
	}

    openMedicalCoverageDialog(): void {
		const dialogRef = this.dialog.open(MedicalCoverageComponent, {
			data: {
				genderId: this.patientData.person?.gender.id,
				identificationNumber: this.patientData.person?.identificationNumber,
				identificationTypeId: this.patientData.person?.identificationTypeId,
				initValues: this.patientMedicalCoverages,
				patientId: this.patientData.id
			}
		});

		dialogRef.afterClosed().subscribe(
			values => {
				if (values) {
					const patientCoverages: PatientMedicalCoverageDto[] =
						values.patientMedicalCoverages.map(s => this.mapperService.toPatientMedicalCoverageDto(s));

					this.patientMedicalCoverageService.addPatientMedicalCoverages(Number(this.data.patientId), patientCoverages).subscribe(
						_ => {
							this.setMedicalCoverages();
							this.snackBarService.showSuccess('ambulatoria.paciente.ordenes_prescripciones.toast_messages.POST_UPDATE_COVERAGE_SUCCESS');
						},
						_ => this.snackBarService.showError('ambulatoria.paciente.ordenes_prescripciones.toast_messages.POST_UPDATE_COVERAGE_ERROR')
					);
				}
			}
		);
	}

    clear(control: AbstractControl): void {
		this.clearControls.emit(control);
		this.showCoverageMessage = false;
	}

    setCities(departmentId: number) {
		if (departmentId)
			this.cities$ = this.addressMasterDataService.getCitiesByDepartment(departmentId);
	}

    setDepartments(provinceId: number) {
		if (provinceId) {
			this.departments$ = this.addressMasterDataService.getDepartmentsByProvince(provinceId);
			this.prescriptionForm.get('patientData.locality').setValue(null);
			this.prescriptionForm.get('patientData.city').setValue(null);
			this.cities$ = null;
		}
	}

    private setMedicalCoverages(): void {
		this.patientMedicalCoverageService.getActivePatientMedicalCoverages(Number(this.data.patientId))
			.pipe(
				map(
					patientMedicalCoveragesDto =>
						patientMedicalCoveragesDto.map(s => this.mapperService.toPatientMedicalCoverage(s))
				)
			)
			.subscribe((patientMedicalCoverages: PatientMedicalCoverage[]) => {
				this.patientMedicalCoverages = patientMedicalCoverages;
			});
	}

	private setProvinces() {
		this.provinces$ = this.addressMasterDataService.getByCountry(ARGENTINA_ID);
	}

	private setCountries() {
		this.getCompletePerson()
			.subscribe((person: BMPersonDto) => {
				this.person = person;
				this.personEmmiter.emit(person);
				this.countries$ = this.addressMasterDataService.getAllCountries();
				this.countries$.subscribe(_ => {
					this.setProvinces();
					this.setDepartments(person.provinceId);
					this.setCities(person.departmentId);
					this.setPatientDataValues(person);
				});
			});
	}

    private setPatientDataValues(person: BMPersonDto) {
		this.prescriptionForm.get('patientData.country').setValue(ARGENTINA_ID);
		this.prescriptionForm.get('patientData.province').setValue(person.provinceId);
		this.prescriptionForm.get('patientData.locality').setValue(person.departmentId);
		this.prescriptionForm.get('patientData.city').setValue(person.cityId);
		this.prescriptionForm.get('patientData.street').setValue(person.street);
		this.prescriptionForm.get('patientData.streetNumber').setValue(person.number);
		this.prescriptionForm.get('patientData.phonePrefix').setValue(person.phonePrefix);
		this.prescriptionForm.get('patientData.phoneNumber').setValue(person.phoneNumber);
	}

	private getCompletePerson(): Observable<BMPersonDto> {
		return this.personService.getCompletePerson<BMPersonDto>(this.data.personId);
	}

}
