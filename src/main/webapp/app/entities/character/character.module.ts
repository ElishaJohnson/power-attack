import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { PowerattackSharedModule } from 'app/shared/shared.module';
import { CharacterComponent } from './character.component';
import { CharacterDetailComponent } from './character-detail.component';
import { CharacterUpdateComponent } from './character-update.component';
import { CharacterDeleteDialogComponent } from './character-delete-dialog.component';
import { characterRoute } from './character.route';

@NgModule({
  imports: [PowerattackSharedModule, RouterModule.forChild(characterRoute)],
  declarations: [CharacterComponent, CharacterDetailComponent, CharacterUpdateComponent, CharacterDeleteDialogComponent],
  entryComponents: [CharacterDeleteDialogComponent]
})
export class PowerattackCharacterModule {}
