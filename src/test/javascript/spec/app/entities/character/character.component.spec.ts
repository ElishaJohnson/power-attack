import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { PowerattackTestModule } from '../../../test.module';
import { CharacterComponent } from 'app/entities/character/character.component';
import { CharacterService } from 'app/entities/character/character.service';
import { Character } from 'app/shared/model/character.model';

describe('Component Tests', () => {
  describe('Character Management Component', () => {
    let comp: CharacterComponent;
    let fixture: ComponentFixture<CharacterComponent>;
    let service: CharacterService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [PowerattackTestModule],
        declarations: [CharacterComponent]
      })
        .overrideTemplate(CharacterComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(CharacterComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(CharacterService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new Character(123)],
            headers
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.characters && comp.characters[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
