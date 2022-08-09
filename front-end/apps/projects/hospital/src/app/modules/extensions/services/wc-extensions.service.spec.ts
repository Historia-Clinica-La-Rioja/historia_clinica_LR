import { Observable, } from 'rxjs';
import { cold } from 'jasmine-marbles';

import { Slot, WCExtensionsService, WCInfo } from './wc-extensions.service';
import { ExtensionComponentDto } from '@extensions/extensions-model';
import { map } from 'rxjs/operators';



// const httpResponse1 = {
// 	data: JSON.stringify({
// 		permissions: ['blah1', 'blah2']
// 	})
// };

// const httpResponse2 = {
// 	data: JSON.stringify({
// 		permissions: ['blah3']
// 	})
// };

const EXTENSIONS: ExtensionComponentDto[] = [
	{ name: 'Notas sobre Pacientes', path: 'notas' },
	{ name: 'Tareas', path: 'tareas' }
];
const DEFINITIONS: { [path: string]: WCInfo[] } = {
	notas: [
		{ slot: 'CLINIC_HISTORY_TAB', url: 'https://wc.hsi.ar/notas.js', componentName: 'note-tab' }
	],
	tareas: [
		{ slot: 'INSTITUTION_HOME_PAGE', url: 'https://wc.hsi.ar/tasks.js', componentName: 'task-list' }
	]
};

describe('WCExtensionsService.extensions$', () => {
	let wcExtensions: WCExtensionsService;
	let extensionsMock;

	beforeEach(() => {
		extensionsMock = jasmine.createSpy('UserService');
		wcExtensions = new WCExtensionsService(extensionsMock);
		wcExtensions.init();
	});

	/*
		it('Should emit empty if there is no extension set', () => {
			extensionsMock.getExtensions = () => cold('-a', { a: [] });
			//	wcExtensions.fetchExtensions();
			expect(wcExtensions.getComponentsFromSlot(Slot.INSTITUTION_HOME_PAGE)).toBeObservable(cold('a', { a: null }));
		});

		it('Ejemplo de marble test', () => {
			// https://mokkapps.de/blog/how-i-write-marble-tests-for-rxjs-observables-in-angular/
			const ejemplo$: Observable<string> = cold('abc', { a: 'primer valor', b: 'segundo valor', c: 'tercer valor' });
			const ejemploSinValor$: Observable<string> = ejemplo$.pipe(
				map(valor => valor.split(' ')[0]),
			);
			ejemplo$.subscribe(valor => console.log('ejemplo$ emite', valor));
			ejemploSinValor$.subscribe(valor => console.log('ejemploSinValor$ emite', valor));
			expect(ejemploSinValor$).toBeObservable(cold('abc', { a: 'primer', b: 'segundo', c: 'tercer' }));
		});

	it('Should work', () => {
		extensionsMock.getExtensions = () => cold('-a', { a: EXTENSIONS });
		extensionsMock.getDefinition = (path: string) => cold('-a', { a: DEFINITIONS[path] });

		wcExtensions.fetchExtensions();
		expect(wcExtensions.getComponentsFromSlot(Slot.INSTITUTION_HOME_PAGE)).toBeObservable(cold('a-b', { a: null, b: [{ "componentName": "task-list", "url": "https://wc.hsi.ar/tasks.js", title: undefined }] }));
	});*/

});


