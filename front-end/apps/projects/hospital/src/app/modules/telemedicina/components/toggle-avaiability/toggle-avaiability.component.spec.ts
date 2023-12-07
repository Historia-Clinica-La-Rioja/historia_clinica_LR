import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ToggleAvaiabilityComponent } from './toggle-avaiability.component';

describe('ToogleAvaiabilityComponent', () => {
  let component: ToggleAvaiabilityComponent;
  let fixture: ComponentFixture<ToggleAvaiabilityComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ToggleAvaiabilityComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ToggleAvaiabilityComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
