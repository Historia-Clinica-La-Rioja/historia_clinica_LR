import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ToggleAvailabilityComponent } from './toggle-availability.component';

describe('ToogleAvaiabilityComponent', () => {
  let component: ToggleAvailabilityComponent;
  let fixture: ComponentFixture<ToggleAvailabilityComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ToggleAvailabilityComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ToggleAvailabilityComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
