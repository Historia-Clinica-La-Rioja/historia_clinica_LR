import { TestBed } from '@angular/core/testing';
import { TypeaheadV2Service } from './typeahead-v2.service';
import { ReactiveFormsModule, UntypedFormBuilder } from '@angular/forms';
import { TypeaheadOption } from '@presentation/components/typeahead/typeahead.component';

export const FACUNDO = { value: '1', compareValue: 'Facundo' }
export const FEDERICO = { value: '2', compareValue: 'Federico' }
const FERNANDO = { value: '3', compareValue: 'Fernando' }

export const mockOptionsTypeahead: TypeaheadOption<any>[] = [
	FACUNDO,
	FEDERICO,
	FERNANDO,
];

describe('TypeaheadV2Service', () => {
	let service: TypeaheadV2Service;

	beforeEach(() => {
		TestBed.configureTestingModule({
			providers: [TypeaheadV2Service, UntypedFormBuilder],
			imports: [ReactiveFormsModule],
		});
		service = TestBed.inject(TypeaheadV2Service);
	});

	it('should be created', () => {
		expect(service).toBeTruthy();
	});

	describe('options filtering', () => {

		it('should return options that match the given letters, having some matches', () => {
			const optionsStartedWith_fe: TypeaheadOption<any>[] = [
				FEDERICO,
				FERNANDO,
			];
			service.filterOptions('fe', mockOptionsTypeahead);
			service.optionfilter$.subscribe(optiones =>
				expect(optiones).toEqual(optionsStartedWith_fe)
			);
		});

		it('should return an empty list if there are no matching options', () => {
			const optionFilterEmty = [];
			service.filterOptions('te', mockOptionsTypeahead);
			service.optionfilter$.subscribe(optiones =>
				expect(optiones).toEqual(optionFilterEmty)
			);
		});
	});

	it('should emit null if attempting to select an item when the options list is empty', () => {
		service.setOptions([]);
		service.select(FACUNDO);
		service.selectValue$.subscribe(e =>
			expect(e).toEqual(null)
		);
	});

	it('should emit null value when field is cleared', () => {
		service.clear();
		service.selectValue$.subscribe(e =>
			expect(e).toEqual(null)
		)
	});

	it('should keep the selected option if the component is disabled and then a new option is attempted to be selected', () => {
		service.setOptions(mockOptionsTypeahead);
		service.select(FACUNDO);
		service.disabled = true;
		service.select(FEDERICO);
		service.selectValue$.subscribe(e =>
			expect(e.value).toBe(FACUNDO.value)
		)
	});

	it('should support the user being able to select an option within the list of options', () => {
		service.setOptions(mockOptionsTypeahead);
		service.select(FEDERICO);
		service.selectValue$.subscribe(e =>
			expect(e.value).toBe(FEDERICO.value)
		);
	});

});
