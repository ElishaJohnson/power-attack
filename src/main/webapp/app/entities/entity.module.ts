import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'character',
        loadChildren: () => import('./character/character.module').then(m => m.PowerattackCharacterModule)
      },
      {
        path: 'attack-cycle',
        loadChildren: () => import('./attack-cycle/attack-cycle.module').then(m => m.PowerattackAttackCycleModule)
      },
      {
        path: 'weapon',
        loadChildren: () => import('./weapon/weapon.module').then(m => m.PowerattackWeaponModule)
      },
      {
        path: 'attack',
        loadChildren: () => import('./attack/attack.module').then(m => m.PowerattackAttackModule)
      }
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ])
  ]
})
export class PowerattackEntityModule {}
