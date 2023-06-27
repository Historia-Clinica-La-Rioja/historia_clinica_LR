import { Injectable } from '@angular/core';
import { ExtensionComponentDto } from '@extensions/extensions-model';
import { BehaviorSubject, Observable, map } from 'rxjs';
import { ExtensionsService } from './extensions.service';
import { WCParams } from '@extensions/components/ui-external-component/ui-external-component.component';
import {
	findSlotedInfoById,
	toClinicHistoryWCParams,
	toSystemHomeWCParams,
	toInstitutionWCParamsList,
	toSystemHomeWCParamsList,
	toMenuItemList,
} from './wc-extensions.mappers';
import { SlotsStorageService } from './storages/slots-storage.service';
import { MenuItem } from '@presentation/components/menu/menu.component';

@Injectable({
	providedIn: 'root'
})
export class WCExtensionsService {

	private emitters: { slot: Slot, emitter: BehaviorSubject<SlotedInfo[]> }[] = [];
	private slotedComponents: { slot: Slot, components$: Observable<SlotedInfo[]> }[] = [];

	constructor(
		private readonly extensionService: ExtensionsService
	) {
		this.init();
		this.fetchExtensions();
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

	getInstitutionHomeComponents(institutionId: number): Observable<WCParams[]> {
		return this.listComponentsFromSlot(Slot.INSTITUTION_HOME_PAGE)
			.pipe(
				map(toInstitutionWCParamsList(institutionId)),
			);
	}


	private listComponentsFromSlot(slot: Slot): Observable<SlotedInfo[]> {
		return this.slotedComponents.find(a => a.slot === slot).components$;
	}

	private fetchExtensions() {

		/**
		 * Cada modulo va a insertar una tupla en la BD
		 * Esa tupla va a tener el nombre del modulo en cuestion a modo informativo
		 * 		y la url en la que tiene un archivo
			Este archvi informa en donde esta cada WC que quiere agregar
		* 		como se llama el wc que quiere agregar
		* 		en donde esta hosteado ese WC
		* 		en que pantalla se quiere ubicar este WC
		* 		parametros para el wc?
		*/

		const allPlugins$: Observable<ExtensionComponentDto[]> = this.extensionService.getExtensions();

		allPlugins$.subscribe(
			allPlugins => {
				allPlugins.forEach(plugin => {
					this.extensionService.getDefinition(plugin.path).subscribe(
						(defPluginArr: WCInfo[]) => {
							console.log('getdef ', defPluginArr);
							const slotsStorageService = new SlotsStorageService(plugin.path);
							defPluginArr.forEach(d => {
								slotsStorageService.put(d);
							});
							slotsStorageService.forEachSlot(
								(slotName, valuesToEmit) =>
									this.emitters.find(sc => sc.slot === slotName).emitter.next(valuesToEmit)
							)
						}
					)
				})
			}
		);
	}

	init() { // Just public for testing
		Object.values(Slot).forEach(
			slot => {
				const emitter = new BehaviorSubject<SlotedInfo[]>([]);
				this.emitters.push({ slot, emitter });
				this.slotedComponents.push({ slot, components$: emitter.asObservable() }); // Initial value null means not fetched, [] means no components
			}
		)
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
	componentName: string;
	url: string;
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
