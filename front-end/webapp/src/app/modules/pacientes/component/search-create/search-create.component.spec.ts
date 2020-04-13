import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { SearchCreateComponent } from './search-create.component';

describe('SearchCreateComponent', () => {
  let component: SearchCreateComponent;
  let fixture: ComponentFixture<SearchCreateComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ SearchCreateComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SearchCreateComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
