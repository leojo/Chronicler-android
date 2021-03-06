package com.android.chronicler.character.feat;

import java.io.Serializable;

/**
 * Created by leo on 28.11.2015.
 *
 * A class representing a single feat. This is only a data structure and has no usability functions.
 */
public class Feat  implements Serializable, Comparable {
    private String id;
    private String name;
    private String type;
    private String multiple;
    private String stack;
    private String choice;
    private String prerequisite;
    private String benefit;
    private String normal;
    private String special;
    private String fullText;
    private String reference;
    private String selectedChoice = "Please select";

    public String shortDescription(){
        String benefitRAW = getBenefit();
        int from = 0;
        int to = benefitRAW.length();

        from = Math.min(Math.max(benefitRAW.indexOf("</b>")+4,from),to);
        to = Math.max(Math.min(benefitRAW.indexOf("</p>")-1,to),from);

        return benefitRAW.substring(from,to);
    }

    //<editor-fold desc="Getters and Setters">

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMultiple() {
        return multiple;
    }

    public void setMultiple(String multiple) {
        this.multiple = multiple;
    }

    public String getStack() {
        return stack;
    }

    public void setStack(String stack) {
        this.stack = stack;
    }

    public String getChoice() {
        return choice;
    }

    public void setChoice(String choice) {
        this.choice = choice;
    }

    public String getPrerequisite() {
        return prerequisite;
    }

    public void setPrerequisite(String prerequisite) {
        this.prerequisite = prerequisite;
    }

    public String getBenefit() {
        return benefit;
    }

    public void setBenefit(String benefit) {
        this.benefit = benefit;
    }

    public String getNormal() {
        return normal;
    }

    public void setNormal(String normal) {
        this.normal = normal;
    }

    public String getSpecial() {
        return special;
    }

    public void setSpecial(String special) {
        this.special = special;
    }

    public String getFullText() {
        return fullText;
    }

    public void setFullText(String fullText) {
        this.fullText = fullText;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getSelectedChoice() {
        return selectedChoice;
    }

    public void setSelectedChoice(String selectedChoice) {
        this.selectedChoice = selectedChoice;
    }

    //</editor-fold>

    @Override
    public int compareTo(Object another) {
        if(another instanceof Feat){
            return name.compareTo(((Feat) another).getName());
        }
        return 0;
    }
}
