import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { TriageDetailsComponent } from './triage-details.component';

describe('TriageDetailsComponent', () => {
	let component: TriageDetailsComponent;
	let fixture: ComponentFixture<TriageDetailsComponent>;

	beforeEach(async(() => {
		TestBed.configureTestingModule({
			declarations: [TriageDetailsComponent]
		})
			.compileComponents();
	}));

	beforeEach(() => {
		fixture = TestBed.createComponent(TriageDetailsComponent);
		component = fixture.componentInstance;
		fixture.detectChanges();
	});

	it('should create', () => {
		expect(component).toBeTruthy();
	});
});
