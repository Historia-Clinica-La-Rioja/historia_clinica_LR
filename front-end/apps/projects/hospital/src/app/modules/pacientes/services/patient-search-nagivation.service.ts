import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { AppFeature, PersonBasicDataResponseDto } from '@api-rest/api-model';
import { PersonService } from '@api-rest/services/person.service';
import { ContextService } from '@core/services/context.service';
import { FeatureFlagService } from '@core/services/feature-flag.service';
import { IDENTIFICATION_TYPE_IDS, PATIENT_TYPE } from '@core/utils/patient.utils';
import { ParamsToSearchPerson } from '@pacientes/component/search-create/search-create.component';
import { encode } from '@pacientes/utils/search.utils';
import { SnackBarService } from '@presentation/services/snack-bar.service';
import { Observable, of, switchMap, map, catchError, Subject } from 'rxjs';

const ROUTE_SEARCH = 'pacientes/search';
const ROUTE_NEW = 'pacientes/new';
const ROUTE_NEW_TEMPORARY = 'pacientes/temporary';

@Injectable({
	providedIn: 'root'
})
export class PatientSearchNagivationService {

	private routePrefix;
	private renaperInformation: PersonBasicDataResponseDto;
	private paramsToSearchPerson: ParamsToSearchPerson;
	private showSpinner = new Subject<boolean>();
	showSpinner$ = this.showSpinner.asObservable();

	constructor(
		private readonly featureFlagService: FeatureFlagService,
		private readonly personService: PersonService,
		private readonly snackBarService: SnackBarService,
		private readonly router: Router,
		private readonly contextService: ContextService,
	) { 
		this.contextService.institutionId$.subscribe(institutionId => this.routePrefix =  `institucion/${institutionId}/`);
	}

	calculateNavigation(paramsToSearchPerson: ParamsToSearchPerson) {
		this.paramsToSearchPerson = paramsToSearchPerson;
		this.showSpinner.next(true);
		!this.hasPatientIdentity().subscribe(hasIdentity => {
			this.showSpinner.next(false);
			hasIdentity ? this.addPerson() : this.goToPersonSearch();
		});
	}

	setSpinner(enableSpinner: boolean) {
		this.showSpinner.next(enableSpinner);
	}

	private hasPatientIdentity(): Observable<boolean> {
		const noIdentity = this.paramsToSearchPerson.noIdentity === true;
		const identificationTypeId = Number(this.paramsToSearchPerson.identificationTypeId);

		const hasNotIdentification = noIdentity || !this.isIdentificationTypeDni(identificationTypeId);
		if (hasNotIdentification)
			return of(false);

		return this.featureFlagService.isActive(AppFeature.HABILITAR_SERVICIO_RENAPER).pipe(
			switchMap(isEnabledRenaper => !isEnabledRenaper ? of(false) : this.isInRenaper()
		));
	}

	private isInRenaper(): Observable<boolean> {
		return this.personService.getRenaperPersonData({
			identificationNumber: this.paramsToSearchPerson.identificationNumber,
			genderId: this.paramsToSearchPerson.genderId
		}).pipe(
			map(renaperData => this.verifyRenaperData(renaperData)),
			catchError(error => {
				this.snackBarService.showError('pacientes.search.RENAPER_TIMEOUT');
				return of(false);
			}),
		);
	}

	private splitStringByFirstSpaceCharacter(text: string): any {
		const spaceIndex: number = text.indexOf(' ');
		if (spaceIndex === 0) {
			return this.splitStringByFirstSpaceCharacter(text.substr(1));
		} else {
			return (spaceIndex !== -1) ?
				{
					firstSubstring: text.substr(0, spaceIndex),
					secondSubstring: text.substr(spaceIndex + 1)
				}
				:
				{ firstSubstring: text };
		}
	}

	private verifyRenaperData(renaperData: PersonBasicDataResponseDto): boolean {
		if (renaperData && Object.keys(renaperData).length !== 0) {
			this.renaperInformation = renaperData;
			return true;
		}
		return false;
	}

	private isIdentificationTypeDni(identificationTypeId: number): boolean {
		return identificationTypeId === IDENTIFICATION_TYPE_IDS.DNI;
	}

	private isIdentificationTypeDoesNotHave(identificationTypeId: number) {
		return identificationTypeId == IDENTIFICATION_TYPE_IDS.NO_POSEE
	}

	private addPerson() {
		if (this.renaperInformation)
			this.setParamsByRenaperInformation();
		const noIdentity = this.paramsToSearchPerson.noIdentity === true;
		const identificationTypeId = +this.paramsToSearchPerson.identificationTypeId;

		const isTemporaryPatient = noIdentity || this.isIdentificationTypeDoesNotHave(identificationTypeId);

		(isTemporaryPatient) ? this.goToCreateTemporaryPerson() : this.goToCreatePerson();
	}

	private setParamsByRenaperInformation() {
		const { firstName, lastName, birthDate, photo, cuil } = this.renaperInformation;
		const splitedFirstName = this.splitStringByFirstSpaceCharacter(firstName);
		const splitedLastName = this.splitStringByFirstSpaceCharacter(lastName);
		this.paramsToSearchPerson = {
			...this.paramsToSearchPerson,
			firstName: splitedFirstName.firstSubstring,
			middleNames: splitedFirstName.secondSubstring,
			lastName: splitedLastName.firstSubstring,
			otherLastNames: splitedLastName.secondSubstring,
			birthDate, photo, cuil,
			typeId: PATIENT_TYPE.VALID
		};
	}

	private goToCreateTemporaryPerson() {
		this.router.navigate([`${this.routePrefix}${ROUTE_NEW_TEMPORARY}`], {
			queryParams: this.paramsToSearchPerson
		});
	}

	private goToCreatePerson() {
		let encryptedPerson = encode(JSON.stringify(this.paramsToSearchPerson));
		this.router.navigate([`${this.routePrefix}${ROUTE_NEW}`], {
			queryParams: {
				person: encryptedPerson
			}
		});
	}

	private goToPersonSearch() {
		this.router.navigate([`${this.routePrefix}${ROUTE_SEARCH}`], {
			queryParams: this.paramsToSearchPerson
		});
	}

}