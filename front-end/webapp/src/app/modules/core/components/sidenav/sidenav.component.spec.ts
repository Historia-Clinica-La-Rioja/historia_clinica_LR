import { ComponentFixture, TestBed, waitForAsync } from '@angular/core/testing';
import { CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { SidenavComponent } from './sidenav.component';

describe('SidenavComponent', () => {
	let component: SidenavComponent;
	let fixture: ComponentFixture<SidenavComponent>;

	beforeEach(waitForAsync(() => {
		TestBed.configureTestingModule({
			declarations: [SidenavComponent],
			schemas: [
				CUSTOM_ELEMENTS_SCHEMA
			],
		})
			.compileComponents();
	}));

	beforeEach(() => {
		fixture = TestBed.createComponent(SidenavComponent);
		component = fixture.componentInstance;
		fixture.detectChanges();
	});

	it('should create', () => {
		expect(component).toBeTruthy();
	});
});
