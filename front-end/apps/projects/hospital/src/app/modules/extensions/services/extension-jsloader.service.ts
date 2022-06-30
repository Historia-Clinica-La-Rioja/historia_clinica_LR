import { Injectable } from '@angular/core';

@Injectable({
	providedIn: 'root'
})
export class ExtensionJSLoaderService {

	constructor() { }

	private urls: Element[] = [];

	addScriptTag(urlToFetch: string): HTMLScriptElement {
		const HTMLScriptElement = this.urls.find(e => e.url === urlToFetch)?.HTMLScriptElement;
		if (HTMLScriptElement)
			return HTMLScriptElement;
		const scriptTag = document.createElement('script');
		scriptTag.src = urlToFetch;
		const script = document.getElementsByTagName('head')[0].appendChild(scriptTag);
		this.urls.push({ url: urlToFetch, HTMLScriptElement: script });
		return script;
	}
}

interface Element {
	url: string;
	HTMLScriptElement: HTMLScriptElement
}
