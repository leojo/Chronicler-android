package com.android.chronicler.character.item;

import com.android.chronicler.character.enums.DamageType;

/**
 * Created by leo on 23.2.2016.
 */
public class Weapon extends Equipment{

    private int damageDie, diceCount, critMult, critRange; // critRange = 2 if it's "19-20" i.e. (upper-lower)+1
    private boolean twoHand, oneHand, ranged, thrown, finessable;
    private DamageType[] damageTypes;
    private int rangeIncrement;
    private Projectile ammo;

    public Weapon(){
        super();
    }

    //TODO: Finish implementing Weapon

}
