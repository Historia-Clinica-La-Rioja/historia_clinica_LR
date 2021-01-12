import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { CardEstudiosComponent } from './card-estudios.component';

describe('CardEstudiosComponent', () => {
  let component: CardEstudiosComponent;
  let fixture: ComponentFixture<CardEstudiosComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ CardEstudiosComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CardEstudiosComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
