import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EdmontonComponent } from './edmonton.component';

describe('EdmontonComponent', () => {
  let component: EdmontonComponent;
  let fixture: ComponentFixture<EdmontonComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ EdmontonComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(EdmontonComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
