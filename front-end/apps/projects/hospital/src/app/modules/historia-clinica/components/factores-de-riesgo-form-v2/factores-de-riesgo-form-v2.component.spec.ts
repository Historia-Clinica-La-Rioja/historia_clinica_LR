import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FactoresDeRiesgoFormV2Component } from './factores-de-riesgo-form-v2.component';

describe('FactoresDeRiesgoFormV2Component', () => {
  let component: FactoresDeRiesgoFormV2Component;
  let fixture: ComponentFixture<FactoresDeRiesgoFormV2Component>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ FactoresDeRiesgoFormV2Component ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(FactoresDeRiesgoFormV2Component);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
