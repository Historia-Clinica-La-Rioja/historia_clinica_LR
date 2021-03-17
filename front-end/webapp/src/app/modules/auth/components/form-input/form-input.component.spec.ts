import { ComponentFixture, TestBed, waitForAsync } from '@angular/core/testing';
import { CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { FormInputComponent } from './form-input.component';

describe('FormInputComponent', () => {
	let component: FormInputComponent;
	let fixture: ComponentFixture<FormInputComponent>;

	beforeEach(waitForAsync(() => {
		TestBed.configureTestingModule({
			declarations: [FormInputComponent],
			schemas: [
				CUSTOM_ELEMENTS_SCHEMA
			],
		})
			.compileComponents();
	}));

	beforeEach(() => {
		fixture = TestBed.createComponent(FormInputComponent);
		component = fixture.componentInstance;
		fixture.detectChanges();
	});

	it('should create', () => {
		expect(component).toBeTruthy();
	});
});
