import { ICharacter } from 'app/shared/model/character.model';

export interface IWeapon {
  id?: number;
  name?: string;
  attackModifier?: number;
  critChance?: number;
  critDamage?: number;
  dieValue?: number;
  diceCount?: number;
  damageBonus?: number;
  character?: ICharacter;
}

export class Weapon implements IWeapon {
  constructor(
    public id?: number,
    public name?: string,
    public attackModifier?: number,
    public critChance?: number,
    public critDamage?: number,
    public dieValue?: number,
    public diceCount?: number,
    public damageBonus?: number,
    public character?: ICharacter
  ) {}
}
