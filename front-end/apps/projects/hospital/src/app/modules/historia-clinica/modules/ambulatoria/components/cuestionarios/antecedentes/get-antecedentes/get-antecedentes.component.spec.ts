import { ComponentFixture, TestBed } from '@angular/core/testing';

import { GetAntecedentesComponent } from './get-antecedentes.component';

describe('GetAntecedentesComponent', () => {
  let component: GetAntecedentesComponent;
  let fixture: ComponentFixture<GetAntecedentesComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ GetAntecedentesComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(GetAntecedentesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
