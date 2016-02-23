package com.android.chronicler.character.item;

import com.android.chronicler.character.enums.DamageType;

/**
 * Created by leo on 23.2.2016.
 */
public class Projectile extends Item {
    private int damageDie, diceCount, rangeIncrement, critMult, critRange; // critRange = 2 if it's "19-20" i.e. (upper-lower)+1
    private DamageType[] damageTypes;
}
