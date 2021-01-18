import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { VerResultadosEstudioComponent } from './ver-resultados-estudio.component';

describe('VerResultadosEstudioComponent', () => {
  let component: VerResultadosEstudioComponent;
  let fixture: ComponentFixture<VerResultadosEstudioComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ VerResultadosEstudioComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(VerResultadosEstudioComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
