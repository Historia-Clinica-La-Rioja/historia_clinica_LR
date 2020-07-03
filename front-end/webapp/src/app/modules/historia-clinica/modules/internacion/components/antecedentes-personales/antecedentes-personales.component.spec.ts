import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AntecedentesPersonalesComponent } from './antecedentes-personales.component';

describe('AntecedentesPersonalesComponent', () => {
  let component: AntecedentesPersonalesComponent;
  let fixture: ComponentFixture<AntecedentesPersonalesComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AntecedentesPersonalesComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AntecedentesPersonalesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
