import { ComponentFixture, TestBed } from '@angular/core/testing';
import { AppointmentTabsComponent } from './appointment-tabs.component';
import { TabsLabel } from '@turnos/constants/tabs';

describe('AppointmentTabsComponent', () => {

	let component: AppointmentTabsComponent;
	let fixture: ComponentFixture<AppointmentTabsComponent>;

	beforeEach(async () => {
		await TestBed.configureTestingModule({
			declarations: [AppointmentTabsComponent]
		})
			.compileComponents();

		fixture = TestBed.createComponent(AppointmentTabsComponent);
		component = fixture.componentInstance;
		fixture.detectChanges();
	});

	it('should create', () => {
		expect(component).toBeTruthy();
	});

	it('should successfully load SeachAppointmentsByProfessionalComponent with Input PROFESSIONAL', () => {
		component.tabLabel = TabsLabel.PROFESSIONAL;
		fixture.detectChanges();

		const selector = 'app-seach-appointments-by-professional';
		const renderedComponent = fixture.debugElement.nativeElement.querySelector(selector);

		expect(renderedComponent).toBeTruthy();
	});

	it('should successfully load SearchAppointmentsBySpecialtyComponent with Input INSTITUTION', () => {
		component.tabLabel = TabsLabel.INSTITUTION;
		fixture.detectChanges();

		const selector = 'app-search-appointments-by-specialty';
		const renderedComponent = fixture.debugElement.nativeElement.querySelector(selector);

		expect(renderedComponent).toBeTruthy();
	});

	it('should successfully load SearchAppointmentsInCareNetworkComponent with Input CARE_NETWORK', () => {
		component.tabLabel = TabsLabel.CARE_NETWORK;
		fixture.detectChanges();

		const selector = 'app-search-appointments-in-care-network';
		const renderedComponent = fixture.debugElement.nativeElement.querySelector(selector);

		expect(renderedComponent).toBeTruthy();
	});

	it('should successfully load ReferenceReportComponent with input REPORT', () => {
		component.tabLabel = TabsLabel.REQUESTS;
		fixture.detectChanges();

		const selector = 'app-reference-report';
		const renderedComponent = fixture.debugElement.nativeElement.querySelector(selector);

		expect(renderedComponent).toBeTruthy();
	});

	it('should successfully load SearchAppointmentsByEquipmentComponent with Input IMAGE_NETWORK', () => {
		component.tabLabel = TabsLabel.IMAGE_NETWORK;
		fixture.detectChanges();

		const selector = 'app-search-appointments-by-equipment';
		const renderedComponent = fixture.debugElement.nativeElement.querySelector(selector);

		expect(renderedComponent).toBeTruthy();
	});

});
