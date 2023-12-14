import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { BasicOption, FormOption } from '../violence-situation-person-information/violence-situation-person-information.component';
import { hasError } from '@core/utils/form.utils';

enum Articulation {
	IN = 'Articulación con otras áreas/organismos del sector salud',
	OUT = 'Articulación con otros organismos fuera del sector salud'
}

enum Area {
	PROVINCIAL_HOSPITAL = 'Hospital provincial',
	HEALTH_REGION = 'Sede región sanitaria',
	UPA = 'UPA',
	CPA = 'CPA',
	POSTAS = 'Postas',
	SIES = 'SIES',
	VACCINATION = 'Vacunatorio',
	CETEC = 'CETEC',
	MINISTRY_HEADQUARTERS = 'Sede Central del Ministerio',
	CAPS = 'CAPS',
	OTHER = 'Otros'
}

enum Establishments {
	CLINIC = 'Clínica médica',
	PEDIATRIC = 'Pediatría',
	GYNECOLOGIC_OBSTRETRIC = 'Ginecología/Obstetricia',
	SOCIAL_WORK = 'Trabajo social',
	MENTAL_HEALTH = 'Salud mental',
	NURSING = 'Enfermería',
	SAPS = 'SAPS',
	EDA = 'EDA',
	EMERGENCY_CARE = 'Guardia',
	AGAINST_VIOLENCE = 'Comité contras las violencias',
	ADDRESS = 'Dirección',
	OTHER = 'Otros'
}

enum InternmentIndication {
	YES = 'Sí',
	YES_PROTECTION_MEASURE = 'Sí, como medida de resguardo',
	NO = 'No',
}

enum Devices {
	MUNICIPAL_DEVICES = 'Dispositivos estatales municipales',
	PROVINCIAL_DEVICES = 'Dispositivos estatales provinciales',
	NATIONAL_DEVICES = 'Dispositivos estatales nacionales',
	SOCIAL_ORGANIZATION = 'Organizaciones sociales y de la sociedad civil'
}

enum MunicipalDevices {
	GENDER_DIVERSITY = 'Área de Género y diversidad',
	AGAINST_VIOLENCE = 'Mesa local contra las Violencias',
	PROTECTION = 'Servicio Local de Promoción y Protección de Derechos de NNyA',
	CHILDHOOD_ADOLESCENCE_AREA = 'Dirección/área de Niñez y Adolescencia',
	SOCIAL_DEVELOPMENT_AREA = 'Área de Desarrollo Social',
	ADDICTION_PREVENTION_AREA = 'Dirección/área de prevención y atención de adicciones',
	EDUCATIONAL_INSTITUTIONS= 'Instituciones educativas'
}

enum ProvincialDevices {
	WOMAN_MINISTERY_GENDER_POLITICS_SEXUAL_DIVERSITY = 'Ministerio de Mujeres, Políticas de Género y Diversidad Sexual',
	RIGHT_PROTECION = 'Servicio Zonal de Promoción y Protección de Derechos de NNyA',
	COMMUNITY_RIGHTS = 'Ministerio de Desarrollo de la Comunidad',
	EDUCATIONAL_INSTITUTIONS = 'Instituciones educativas',
	SECURITY_FORCES = 'Fuerzas de seguridad/Comisarías',
	JUDICAL_SYSTEM = 'Sistema judicial/juzgados de paz y de familia',
	PATRONAGE = 'Patronato de liberados',
	JUVENILE_PRISON = 'Instituciones del sistema de responsabilidad penal juvenil',
	JUSTICE_RIGHTS = 'Ministerio de Justicia y Derechos Humanos'
}

enum NationalDevices {
	WOMAN_MINISTERY_GENDER_DIVERSITY = 'Ministerio de Mujeres, Géneros y Diversidad',
	SECRETARY = 'Secretaría Nacional de Niñez, Adolescencia y Familia',
	SOCIAL_DEVELOPMENT = 'Ministerio de Desarrollo Social',
	SEDRONAR = 'SEDRONAR',
	ANSES = 'ANSES',
	PERSON_REGISTRY = 'Registro de las personas',
	EDUCATIONAL_INSTITUTIONS = 'Instituciones educativas',
	SECURITY_FORCES = 'Fuerzas de seguridad',
	JUSTICE_RIGHTS = 'Ministerio de Justicia y Derechos Humanos'
}

enum Organizations {
	POLICE_STATION = 'Comisaría',
	WOMEN_OFFICE_POLICE_STATION = 'Comisaría/Oficina de la Mujer',
	PROSECUTOR = 'Fiscalía',
	FAMILY_COURT = 'Juzgado de la Familia',
	PEACE_COURT = 'Juzgado de Paz'
}

enum Complaints {
	INJURIES = 'Lesiones graves o gravísimas a personas adultas',
	VIOLENCE = 'Violencias contra niñeces y adolescencias',
	OTHER = 'Otro'
}

enum OrganizationsExtended {
	COMPLAINT = 'Denuncia medio digital de seguridad pasa a fiscalía',
	OTHER = 'Otro'
}

enum ImplementedActions {
	ITS = 'Indicación de estudios de laboratorio para determinar ITS',
	VIH_ITS_HEPATITIS = 'Profilaxis para VIH, ITS y Hepatitis virales frente la exposición a fluidos potencialmente infecciosos',
	EMERGENCY = 'Indicación de anticoncepción de emergencia',
	ILE = 'Consejería de acceso a Interrupción Legal de Embarazo (ILE) frente a confirmación de embarazo producto de violación.'
}

@Component({
	selector: 'app-violence-situation-implemented-actions',
	templateUrl: './violence-situation-implemented-actions.component.html',
  	styleUrls: ['./violence-situation-implemented-actions.component.scss']
})
export class ViolenceSituationImplementedActionsComponent implements OnInit {

	form: FormGroup<{
		articulation: FormControl<string>,
		healthSystemArticulation: FormControl<boolean>,
		area: FormControl<string[]>,
		otherArea: FormControl<string>,
		articulationEstablishment: FormControl<boolean>,
		articulationEstablishmentList: FormControl<string[]>,
		otherArticulationEstablishment: FormControl<string>
		internmentIndication: FormControl<string>,
		devices: FormControl<string[]>,
		municipalDevices: FormControl<string[]>,
		provincialDevices: FormControl<string[]>,
		nationalDevices: FormControl<string[]>
		personComplaint: FormControl<boolean>,
		agencyComplaint: FormControl<string[]>,
		isInstitutionComplaint: FormControl<boolean>,
		institutionComplaints: FormControl<string[]>
		institutionComplaintsOrganizations: FormControl<string[]>
		autorityName: FormControl<string>,
		isSexualViolence: FormControl<boolean>,
		implementedActions: FormControl<string[]>
	}>;

	organizationsExtendedEnum = OrganizationsExtended;

	devicesEnum = Devices;

	selectedComplains: string[] = [];

	implementedActions: string[] = [ImplementedActions.ITS, ImplementedActions.VIH_ITS_HEPATITIS, ImplementedActions.EMERGENCY, ImplementedActions.ILE];

	municipalDevicesList: string[] = [MunicipalDevices.GENDER_DIVERSITY, MunicipalDevices.AGAINST_VIOLENCE, MunicipalDevices.PROTECTION, 
									MunicipalDevices.CHILDHOOD_ADOLESCENCE_AREA, MunicipalDevices.SOCIAL_DEVELOPMENT_AREA, MunicipalDevices.ADDICTION_PREVENTION_AREA,
									MunicipalDevices.EDUCATIONAL_INSTITUTIONS];

	provincialDevicesList: string[] = [ProvincialDevices.WOMAN_MINISTERY_GENDER_POLITICS_SEXUAL_DIVERSITY, ProvincialDevices.RIGHT_PROTECION, ProvincialDevices.COMMUNITY_RIGHTS,
										ProvincialDevices.EDUCATIONAL_INSTITUTIONS, ProvincialDevices.SECURITY_FORCES, ProvincialDevices.JUDICAL_SYSTEM,
										ProvincialDevices.PATRONAGE, ProvincialDevices.JUVENILE_PRISON, ProvincialDevices.JUSTICE_RIGHTS];
	
	nationalDevicesList: string[] = [NationalDevices.WOMAN_MINISTERY_GENDER_DIVERSITY, NationalDevices.SECRETARY, NationalDevices.SOCIAL_DEVELOPMENT,
									NationalDevices.SEDRONAR, NationalDevices.ANSES, NationalDevices.PERSON_REGISTRY, NationalDevices.EDUCATIONAL_INSTITUTIONS,
									NationalDevices.SECURITY_FORCES, NationalDevices.JUSTICE_RIGHTS];

	organizations: string[] = [Organizations.POLICE_STATION, Organizations.WOMEN_OFFICE_POLICE_STATION, Organizations.PROSECUTOR,
									Organizations.FAMILY_COURT, Organizations.PEACE_COURT];

	organizationsExtended: string[] = [...this.organizations, OrganizationsExtended.COMPLAINT, OrganizationsExtended.OTHER];

	complaints: string[] = [Complaints.INJURIES, Complaints.VIOLENCE, Complaints.OTHER];

	selectedDevices: string[] = [];

	selectedMunicipalDevices: string[] = [];

	selectedProvincialDevices: string[] = [];

	selectedNationalDevices: string[] = [];

	selectedImplementedActions: string[] = [];

	establishments: string[] = [Establishments.CLINIC, Establishments.PEDIATRIC, Establishments.GYNECOLOGIC_OBSTRETRIC, 
								Establishments.SOCIAL_WORK, Establishments.MENTAL_HEALTH, Establishments.NURSING,
								Establishments.SAPS, Establishments.EDA, Establishments.EMERGENCY_CARE,
								Establishments.AGAINST_VIOLENCE, Establishments.ADDRESS, Establishments.OTHER];

	articulations: string[] = [Articulation.IN, Articulation.OUT];

	intermentIndicationOptions: string[] = [InternmentIndication.YES, InternmentIndication.YES_PROTECTION_MEASURE, InternmentIndication.NO];

	establishmentsEnum = Establishments;

	articulationEnum = Articulation;

	formOption = FormOption;

	areaEnum = Area;

	areas: string[] = [Area.PROVINCIAL_HOSPITAL, Area.HEALTH_REGION, Area.UPA, Area.CPA, Area.POSTAS, Area.SIES, Area.VACCINATION, Area.CETEC, Area.MINISTRY_HEADQUARTERS, Area.CAPS, Area.OTHER];

	basicOptions: BasicOption[] = [
		{
			text: FormOption.YES,
			value: true
		},
		{
			text: FormOption.NO,
			value: false
		},
	];

	hasError = hasError;

	constructor() { }

	ngOnInit(): void {
		this.form = new FormGroup({
			articulation: new FormControl(null, Validators.required),
			healthSystemArticulation: new FormControl(false, Validators.required),
			area: new FormControl([], Validators.required),
			otherArea: new FormControl(null, Validators.required),
			articulationEstablishment: new FormControl(false, Validators.required),
			articulationEstablishmentList: new FormControl([], Validators.required),
			otherArticulationEstablishment: new FormControl(null, Validators.required),
			internmentIndication: new FormControl(null, Validators.required),
			devices: new FormControl([], Validators.required),
			municipalDevices: new FormControl([], Validators.required),
			provincialDevices: new FormControl([], Validators.required),
			nationalDevices: new FormControl([], Validators.required),
			personComplaint: new FormControl(false, Validators.required),
			agencyComplaint: new FormControl([], Validators.required),
			isInstitutionComplaint: new FormControl(false, Validators.required),
			institutionComplaints: new FormControl([], Validators.required),
			institutionComplaintsOrganizations: new FormControl([], Validators.required),
			autorityName: new FormControl(null, Validators.required),
			isSexualViolence: new FormControl(false, Validators.required),
			implementedActions: new FormControl([], Validators.required),
		});
	}

	setImplementedActions(iac: string) {
		this.pushOrRemove(
			iac, 
			this.selectedImplementedActions, 
			this.form.controls.implementedActions
		);
	}

	setComplaints(com: string) {
		this.pushOrRemove(
			com, 
			this.selectedComplains, 
			this.form.controls.institutionComplaints
		);
	}

	setMunicipalDevices(mdev: string) {
		this.pushOrRemove(
				mdev, 
				this.selectedMunicipalDevices, 
				this.form.controls.municipalDevices
			);
	}

	setProvincialDevices(pdev: string) {
		this.pushOrRemove(
				pdev, 
				this.selectedProvincialDevices, 
				this.form.controls.provincialDevices
			);
	}

	setNationalDevices(ndev: string) {
		this.pushOrRemove(
				ndev, 
				this.selectedNationalDevices, 
				this.form.controls.nationalDevices
			);
	}

	setDevice(device: string) {
		this.pushOrRemove(
			device, 
			this.selectedDevices, 
			this.form.controls.devices
		);
	}

	private pushOrRemove(value: string, array: string[], formControl: FormControl) {
		if (array.includes(value)) 
			array.splice(array.indexOf(value), 1);
		else 
			array.push(value);
		
		formControl.setValue(array);
	}

	get articulation() {
		return this.form.value.articulation;
	}

	get area() {
		return this.form.value.area;
	}

	get healthSystemArticulation() {
		return this.form.value.healthSystemArticulation;
	}

	get articulationEstablishment() {
		return this.form.value.articulationEstablishment;
	}

	get articulationEstablishmentList() {
		return this.form.value.articulationEstablishmentList;
	}

	get personComplaint() {
		return this.form.value.personComplaint
	}

	get devices() {
		return this.form.value.devices;
	}

	get isInstitutionComplaint() {
		return this.form.value.isInstitutionComplaint;
	}

	get institutionComplaintsOrganizations() {
		return this.form.value.institutionComplaintsOrganizations;
	}

	get isSexualViolence() {
		return this.form.value.isSexualViolence;
	}

}
