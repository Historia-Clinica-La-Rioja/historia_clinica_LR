import { ComponentFixture, TestBed, waitForAsync } from '@angular/core/testing';
import { CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { AuthComponent } from './auth.component';

describe('AuthComponent', () => {
	let component: AuthComponent;
	let fixture: ComponentFixture<AuthComponent>;

	beforeEach(waitForAsync(() => {
		TestBed.configureTestingModule({
			declarations: [AuthComponent],
			schemas: [
				CUSTOM_ELEMENTS_SCHEMA
			],
		})
			.compileComponents();
	}));

	beforeEach(() => {
		fixture = TestBed.createComponent(AuthComponent);
		component = fixture.componentInstance;
		fixture.detectChanges();
	});

	it('should create', () => {
		expect(component).toBeTruthy();
	});
});
