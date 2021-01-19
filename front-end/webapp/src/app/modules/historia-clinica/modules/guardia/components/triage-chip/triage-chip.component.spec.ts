import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { TriageChipComponent } from './triage-chip.component';

describe('TriageChipComponent', () => {
	let component: TriageChipComponent;
	let fixture: ComponentFixture<TriageChipComponent>;

	beforeEach(async(() => {
		TestBed.configureTestingModule({
			declarations: [TriageChipComponent]
		})
			.compileComponents();
	}));

	beforeEach(() => {
		fixture = TestBed.createComponent(TriageChipComponent);
		component = fixture.componentInstance;
		fixture.detectChanges();
	});

	it('should create', () => {
		expect(component).toBeTruthy();
	});
});
