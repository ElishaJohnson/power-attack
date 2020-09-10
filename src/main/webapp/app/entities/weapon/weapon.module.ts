import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { PowerattackSharedModule } from 'app/shared/shared.module';
import { WeaponComponent } from './weapon.component';
import { WeaponDetailComponent } from './weapon-detail.component';
import { WeaponUpdateComponent } from './weapon-update.component';
import { WeaponDeleteDialogComponent } from './weapon-delete-dialog.component';
import { weaponRoute } from './weapon.route';

@NgModule({
  imports: [PowerattackSharedModule, RouterModule.forChild(weaponRoute)],
  declarations: [WeaponComponent, WeaponDetailComponent, WeaponUpdateComponent, WeaponDeleteDialogComponent],
  entryComponents: [WeaponDeleteDialogComponent]
})
export class PowerattackWeaponModule {}
