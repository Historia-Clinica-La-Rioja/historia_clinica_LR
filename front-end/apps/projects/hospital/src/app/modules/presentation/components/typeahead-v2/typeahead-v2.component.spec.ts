import { ComponentFixture, TestBed } from '@angular/core/testing';
import { TypeaheadV2Component } from './typeahead-v2.component';
import { ReactiveFormsModule } from '@angular/forms';
import { MatInputModule } from '@angular/material/input';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatAutocompleteModule } from '@angular/material/autocomplete';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';
import { FACUNDO, mockOptionsTypeahead } from '@presentation/services/typeahead-v2.service.spec';

describe('TypeaheadV2Component', () => {
	let component: TypeaheadV2Component;
	let fixture: ComponentFixture<TypeaheadV2Component>;

	beforeEach(async () => {
		await TestBed.configureTestingModule({
			declarations: [TypeaheadV2Component],
			imports: [
				ReactiveFormsModule,
				MatInputModule,
				MatFormFieldModule,
				MatAutocompleteModule,
				NoopAnimationsModule
			]
		})
			.compileComponents();

		fixture = TestBed.createComponent(TypeaheadV2Component);
		component = fixture.componentInstance;
		fixture.detectChanges();
	});

	it('should create', () => {
		expect(component).toBeTruthy();
	});

	it('should set the options correctly', () => {
		component.options = mockOptionsTypeahead;
		component.typeaheadService.optionfilter$.subscribe(options =>
			expect(mockOptionsTypeahead).toEqual(options)
		);
	});

	it('should handle the setExternalSetValue input appropriately with an external value, emitting the same value that was set', () => {
		spyOn(component.selectionChange, 'emit');
		component.options = mockOptionsTypeahead;
		component.externalSetValue = FACUNDO;
		expect(component.selectionChange.emit).toHaveBeenCalledWith(FACUNDO.value);
	});


});
