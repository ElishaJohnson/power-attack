import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { ICharacter, Character } from 'app/shared/model/character.model';
import { CharacterService } from './character.service';
import { CharacterComponent } from './character.component';
import { CharacterDetailComponent } from './character-detail.component';
import { CharacterUpdateComponent } from './character-update.component';

@Injectable({ providedIn: 'root' })
export class CharacterResolve implements Resolve<ICharacter> {
  constructor(private service: CharacterService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ICharacter> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((character: HttpResponse<Character>) => {
          if (character.body) {
            return of(character.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Character());
  }
}

export const characterRoute: Routes = [
  {
    path: '',
    component: CharacterComponent,
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Characters'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: CharacterDetailComponent,
    resolve: {
      character: CharacterResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Characters'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: CharacterUpdateComponent,
    resolve: {
      character: CharacterResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Characters'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: CharacterUpdateComponent,
    resolve: {
      character: CharacterResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Characters'
    },
    canActivate: [UserRouteAccessService]
  }
];
