import {
	Component,
	Input,
	ElementRef,
	OnChanges,
	SimpleChanges,
	Renderer2,
} from '@angular/core';
import { Router } from '@angular/router';
import { CustomEventsHandlerService } from '@core/services/custom-events-handler.service';
import { ExtensionJSLoaderService } from '@extensions/services/extension-jsloader.service';
@Component({
	selector: 'app-ui-external-component',
	//the template is empty initially, but filled with content at runtime
	template: ``,
	styleUrls: ['./ui-external-component.component.scss'],
})
export class UiExternalComponentComponent implements OnChanges {
	@Input() type: string;
	@Input() set params (p: WCParams) {
		this._params = p;
		this.customEventsHandlerService.handleEveryEventFromElement(this.renderer2, { absolutUrl: this.router.url, extensionName: p.title })
	}
	private _params: WCParams;
	constructor(
		//inject the elRef object to interact with the template
		private elRef: ElementRef<HTMLElement>,
		private readonly extensionJSLoaderService: ExtensionJSLoaderService,
		private readonly customEventsHandlerService: CustomEventsHandlerService,
		private readonly router: Router,
		private readonly renderer2: Renderer2
	) { }

	public ngOnChanges(_changes: SimpleChanges): void {
		//remove and add pluggins every time any data-bound property of a directive changes
		this.removePlugins();
		this.addPlugins();
	}

	private removePlugins(): void {
		const hostElement = this.elRef.nativeElement;
		let child = hostElement.lastElementChild;
		//remove the last child of the hostElement while it exists
		while (!!child) {
			hostElement.removeChild(child);
			child = hostElement.lastElementChild;
		}
	}

	private addPlugins(): void {
		const fetchedComponent = customElements.get(this._params.componentName);
		const hostElement = this.elRef.nativeElement;
		if (fetchedComponent) {
			this.setCustomElement(
				hostElement,
				this._params,
			);
			return;
		}
		const script = this.extensionJSLoaderService.addScriptTag(this._params.url + `?ts=${Date.now()}`);
		script.addEventListener("load", () => {
			const exist = customElements.get(this._params.componentName);
			if (!exist) {
				console.warn(`Custom element ${this._params.componentName} does not exist`);
				return;
			}
			this.setCustomElement(
				hostElement,
				this._params
			);
		});
	}

	private setCustomElement(hostElement: HTMLElement, params: WCParams) {
		const customElement = document.createElement(params.componentName);
		customElement.setAttribute('params', JSON.stringify(this._params));
		// append the element, so it's attached to the DOM
		hostElement.appendChild(customElement);
	}
}

export interface WCParams {
	title: string;
	componentName: string;
	url: string;
	params: {
		[key:string]: any
	}
}
