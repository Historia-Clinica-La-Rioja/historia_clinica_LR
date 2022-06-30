import { Injectable } from '@angular/core';
import { ExtensionComponentDto } from '@extensions/extensions-model';
import { BehaviorSubject, Observable } from 'rxjs';
import { ExtensionsService } from './extensions.service';


@Injectable({
	providedIn: 'root'
})
export class WCExtensionsService {

	private readonly institutionEmitter = new BehaviorSubject<SlotedInfo[]>(null);
	institutionsExtension$: Observable<any[]> = this.institutionEmitter.asObservable();

	private readonly emmiters = {
		[Slot.INSTITUTION_HOME_PAGE]: this.institutionEmitter,
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
		}
		const allPlugins$: Observable<ExtensionComponentDto[]> = this.extensionService.getExtensions();

		allPlugins$.subscribe(
			allPlugins => {
				allPlugins.forEach(plugin => {
					this.fetchDefinicion(plugin.path).subscribe(
						(defPluginArr: WCInfo[]) => {

							defPluginArr.forEach(d => {
								valuesToEmit[d.slot].push(this.map(d))
							});
							const newWC = new Set(defPluginArr.map(e => e.slot));
							newWC.forEach(key => {
								this.emmiters[key].next(valuesToEmit[key]);
							})
						}
					)
				})
			}
		);
	}

	private fetchDefinicion(url: string): Observable<WCInfo[]> {
		return this.extensionService.getDefinition(url);
	}

	private map(element: WCInfo): SlotedInfo {
		return {
			params: {
				componentName: element.componentName,
				url: element.url,
			}
		}
	}

}

export interface WCInfo {
	slot: string;
	url: string;
	componentName: string;
	params?: {
	}
}


export interface SlotedInfo {

}

export enum Slot {
	INSTITUTION_HOME_PAGE = 'INSTITUTION_HOME_PAGE',
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
