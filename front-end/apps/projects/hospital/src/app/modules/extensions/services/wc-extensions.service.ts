import { Injectable } from '@angular/core';
import { ExtensionComponentDto } from '@extensions/extensions-model';
import {
	EMPTY,
	Observable,
	ReplaySubject,
	catchError,
	map,
	startWith,
} from 'rxjs';
import { ExtensionsService } from './extensions.service';
import { WCParams } from '@extensions/components/ui-external-component/ui-external-component.component';
import {
	findSlotedInfoById,
	toClinicHistoryWCParams,
	toSystemHomeWCParams,
	toInstitutionWCParamsList,
	toSystemHomeWCParamsList,
	toMenuItemList,
	toInstitutionHomeWCParams,
	toPatientProfileWCParams,
} from './wc-extensions.mappers';
import { MenuItem } from '@presentation/components/menu/menu.component';
import { ExtensionsWCService } from './storages/extensions-wc.service';
import { SlotsStorageService } from './storages/slots-storage.service';

@Injectable({
	providedIn: 'root'
})
export class WCExtensionsService {

	private extensions = new ReplaySubject<ExtensionsWCService>(1);
	private webComponents = new ReplaySubject<SlotsStorageService>(1);

	constructor(
		private readonly extensionService: ExtensionsService,
	) {
	}

	getSystemHomeComponents(): Observable<WCParams[]> {
		return this.listComponentsFromSlot(Slot.SYSTEM_HOME_PAGE)
			.pipe(
				map(toSystemHomeWCParamsList)
			);
	}

	getSystemHomeMenu(): Observable<MenuItem[]> {
		return this.listComponentsFromSlot(Slot.HOME_MENU)
			.pipe(
				map(toMenuItemList),
			);
	}

	getSystemHomeMenuPage(wcId: string): Observable<WCParams> {
		return this.listComponentsFromSlot(Slot.HOME_MENU)
			.pipe(
				map(findSlotedInfoById(wcId)),
				map(toSystemHomeWCParams),
			);
	}

	getClinicHistoryComponents(patientId: number): Observable<WCParams[]> {
		return this.listComponentsFromSlot(Slot.CLINIC_HISTORY_TAB)
			.pipe(
				map(sloted => sloted.map(toClinicHistoryWCParams(patientId)))
			);
	}
	getPatientProfileComponents(patientId: number): Observable<WCParams[]> {
		return this.listComponentsFromSlot(Slot.PATIENT_PROFILE)
			.pipe(
				map(sloted => sloted.map(toPatientProfileWCParams(patientId)))
			);
	}

	getInstitutionHomeComponents(institutionId: number): Observable<WCParams[]> {
		return this.listComponentsFromSlot(Slot.INSTITUTION_HOME_PAGE)
			.pipe(
				map(toInstitutionWCParamsList(institutionId)),
			);
	}

	getInstitutionMenu(): Observable<any> {
		return this.listComponentsFromSlot(Slot.INSTITUTION_MENU)
			.pipe(
				map(toMenuItemList),
			)
	}

	getInstitutionMenuPage(wcId: string, institutionId: number): Observable<WCParams> {
		return this.listComponentsFromSlot(Slot.INSTITUTION_MENU)
			.pipe(
				map(findSlotedInfoById(wcId)),
				map(data => toInstitutionHomeWCParams(data, institutionId))
			);
	}

	private listComponentsFromSlot(slot: Slot): Observable<SlotedInfo[]> {
		return this.webComponents.asObservable()
			.pipe(
				map(e => e.wcForSlot(slot)),
				startWith([]), // Emitir un array vacío como valor inicial
			);
	}

	public fetchExtensions() {
		this.extensionService.getExtensions().subscribe(
			extensionDefinitionList => {
				const extensionsSlotsService = this.newExtensionsWCService(extensionDefinitionList);
				extensionDefinitionList.forEach(extensionDefinition => {
					this.extensionService.getDefinition(extensionDefinition.path).pipe(
						catchError(error => {
							console.warn(`No se pudo cargar ${extensionDefinition.name}`, error);
							return EMPTY;
						}),
					).subscribe(
						(defPluginArr: WCInfo[]) => {
							extensionsSlotsService.loaded(extensionDefinition, defPluginArr);
							this.webComponents.next(extensionsSlotsService.wcSlots);
						}
					)
				})
			}
		);
	}

	private newExtensionsWCService(extensions: ExtensionComponentDto[]): ExtensionsWCService {
		const extensionsWCService = new ExtensionsWCService(extensions);
		this.extensions.next(extensionsWCService);
		return extensionsWCService;
	}



}

export interface WCInfo {
	slot: string;
	url: string;
	componentName: string;
	title?: string;
	params?: {
	}
}


export interface SlotedInfo {
	slot: Slot;
	componentName: string;
	fullUrl: string;
	title?: string;
}

export enum Slot {
	// Un componente mas de la pantalla inicial del sistema
	SYSTEM_HOME_PAGE = 'SYSTEM_HOME_PAGE',

	// Un menú mas a nivel sistema
	HOME_MENU = 'HOME_MENU',

	// Un componente mas de la pantalla inicial de la institucion
	INSTITUTION_HOME_PAGE = 'INSTITUTION_HOME_PAGE',

	// Un menú mas a nivel institución
	INSTITUTION_MENU = 'INSTITUTION_MENU',

	// Una solapa mas en la historia clinica
	CLINIC_HISTORY_TAB = 'CLINIC_HISTORY_TAB',

	// Un card mas en el perfil del paciente
	PATIENT_PROFILE = 'PATIENT_PROFILE',
}


/*
		* Hacerlo de esta manera es beneficioso porque se pueden agregar y desactivar plugins dinamicamente
		*
		* Que cada modulo deploye sus WC en archivos separados permite que los equipos no colisionen
		* ya que cada uno va a trabajar en su archivo y asi se evitan fricciones de equipos.
		*
		*
		* No queremos que la carga de WC de un modulo afecte la vision de todos los WC en una pantalla
		* Por eso es que vamos a permitirle a usuario ver los WC a medida que cada uno se completen
		* --> El porque usamos los subjects.
		*
		*/
