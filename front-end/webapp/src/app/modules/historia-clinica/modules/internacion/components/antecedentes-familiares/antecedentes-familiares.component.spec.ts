import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AntecedentesFamiliaresComponent } from './antecedentes-familiares.component';

describe('AntecedentesFamiliaresComponent', () => {
  let component: AntecedentesFamiliaresComponent;
  let fixture: ComponentFixture<AntecedentesFamiliaresComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AntecedentesFamiliaresComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AntecedentesFamiliaresComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
