import { ComponentFixture, TestBed } from '@angular/core/testing';

import { OdontologyComponent } from './odontology.component';

describe('OdontologyComponent', () => {
  let component: OdontologyComponent;
  let fixture: ComponentFixture<OdontologyComponent>;

  beforeEach(async () => {
	await TestBed.configureTestingModule({
		declarations: [ OdontologyComponent ]
	})
	.compileComponents();
  });

  beforeEach(() => {
	fixture = TestBed.createComponent(OdontologyComponent);
	component = fixture.componentInstance;
	fixture.detectChanges();
  });

  it('should create', () => {
	expect(component).toBeTruthy();
  });
});
