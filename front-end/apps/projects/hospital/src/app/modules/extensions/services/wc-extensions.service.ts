import { Injectable } from '@angular/core';
import { ExtensionComponentDto } from '@extensions/extensions-model';
import { BehaviorSubject, Observable } from 'rxjs';
import { ExtensionsService } from './extensions.service';


const slotedInfoMapper = (definition: ExtensionComponentDto) => (element: WCInfo): SlotedInfo  => ({
	componentName: element.componentName,
	url: (new URL(element.url, definition.path)).toString(),
	title: element.title,
});

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

	getComponentsFromSlot(slot: Slot): Observable<SlotedInfo[]> {
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

		const valuesToEmit = {
			[Slot.INSTITUTION_HOME_PAGE]: [],
			[Slot.CLINIC_HISTORY_TAB]: [],
			[Slot.HOME_MENU]: [],
			[Slot.SYSTEM_HOME_PAGE]: [],
		}
		const allPlugins$: Observable<ExtensionComponentDto[]> = this.extensionService.getExtensions();

		allPlugins$.subscribe(
			allPlugins => {
				allPlugins.forEach(plugin => {
					const mapper = slotedInfoMapper(plugin);
					this.fetchDefinicion(plugin.path).subscribe(
						(defPluginArr: WCInfo[]) => {

							defPluginArr.forEach(d => {
								if (!valuesToEmit[d.slot]) {
									console.warn(`Extension ${d.slot} inexistente`);
								} else {
									valuesToEmit[d.slot].push(mapper(d));
								}

							});
							const filledSlotsNames = this.allUsedExtensions(defPluginArr);
							filledSlotsNames.forEach(slotName => {
								this.emitters.find(sc => sc.slot === slotName).emitter.next(valuesToEmit[slotName]);
							})
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

	private allUsedExtensions(defPluginArr: WCInfo[]): Set<string> { // Cambiar a que devuelva slot
		return new Set(defPluginArr.map(e => e.slot).
			filter(value => Object.keys(Slot).some(a => a == value)));
	}

	private fetchDefinicion(url: string): Observable<WCInfo[]> {
		return this.extensionService.getDefinition(url);
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
	HOME_MENU = 'HOME_MENU',
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
