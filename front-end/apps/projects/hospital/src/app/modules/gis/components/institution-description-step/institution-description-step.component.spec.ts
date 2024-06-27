import { ComponentFixture, TestBed } from '@angular/core/testing';

import { InstitutionDescriptionStepComponent } from './institution-description-step.component';

describe('InstitutionDescriptionStepComponent', () => {
  let component: InstitutionDescriptionStepComponent;
  let fixture: ComponentFixture<InstitutionDescriptionStepComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ InstitutionDescriptionStepComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(InstitutionDescriptionStepComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
