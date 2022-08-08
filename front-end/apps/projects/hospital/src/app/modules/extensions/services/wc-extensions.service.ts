import { Injectable } from '@angular/core';
import { ExtensionComponentDto } from '@extensions/extensions-model';
import { BehaviorSubject, Observable } from 'rxjs';
import { ExtensionsService } from './extensions.service';


@Injectable({
	providedIn: 'root'
})
export class WCExtensionsService {

	private readonly institutionEmitter = new BehaviorSubject<SlotedInfo[]>(null);
	institutionsExtension$: Observable<SlotedInfo[]> = this.institutionEmitter.asObservable();

	private readonly clinicHistoryTabsEmitter = new BehaviorSubject<SlotedInfo[]>(null);
	clinicHistoryTabsExtensions$: Observable<SlotedInfo[]> = this.clinicHistoryTabsEmitter.asObservable();

	private readonly homeMenuEmitter = new BehaviorSubject<SlotedInfo[]>(null);
	homeMenuExtension$: Observable<SlotedInfo[]> = this.homeMenuEmitter.asObservable();

	private readonly emmiters = {
		[Slot.INSTITUTION_HOME_PAGE]: this.institutionEmitter,
		[Slot.CLINIC_HISTORY_TAB]: this.clinicHistoryTabsEmitter,
		[Slot.HOME_MENU]: this.homeMenuEmitter
	}
	constructor(
		private readonly extensionService: ExtensionsService
	) { }

	fetchExtensions() {

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

		const valuesToEmit = {
			[Slot.INSTITUTION_HOME_PAGE]: [],
			[Slot.CLINIC_HISTORY_TAB]: [],
			[Slot.HOME_MENU]: []
		}
		const allPlugins$: Observable<ExtensionComponentDto[]> = this.extensionService.getExtensions();

		allPlugins$.subscribe(
			allPlugins => {
				allPlugins.forEach(plugin => {
					this.fetchDefinicion(plugin.path).subscribe(
						(defPluginArr: WCInfo[]) => {

							defPluginArr.forEach(d => {
								if (!valuesToEmit[d.slot]) {
									console.warn(`Extension ${d.slot} inexistente`);
								} else {
									valuesToEmit[d.slot].push(this.map(d))
								}

							});
							const newWC = this.allUsedExtensions(defPluginArr);
							newWC.forEach(key => {
								this.emmiters[key].next(valuesToEmit[key]);
							})
						}
					)
				})
			}
		);
	}

	private allUsedExtensions(defPluginArr: WCInfo[]): Set<string> {
		return new Set(defPluginArr.map(e => e.slot).
			filter(value => Object.keys(Slot).some(a => a == value)));
	}

	private fetchDefinicion(url: string): Observable<WCInfo[]> {
		return this.extensionService.getDefinition(url);
	}

	private map(element: WCInfo): SlotedInfo {
		return {
			componentName: element.componentName,
			url: element.url,
			title: element.title
		}
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
	INSTITUTION_HOME_PAGE = 'INSTITUTION_HOME_PAGE',
	CLINIC_HISTORY_TAB = 'CLINIC_HISTORY_TAB',
	SYSTEM_HOME_PAGE = 'SYSTEM_HOME_PAGE',
	HOME_MENU = 'HOME_MENU'
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
