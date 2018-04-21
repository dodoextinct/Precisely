package com.pankaj.maukascholars.util;

/**
 * Created by hitesh on 19/04/18.
 */

public class LanguageDetails {
    private String language;
    private String language_id;

    public LanguageDetails() {
    }

    public LanguageDetails(String language, String language_id) {
        this.language = language;
        this.language_id = language_id;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getLanguage_id() {
        return language_id;
    }

    public void setLanguage_id(String language_id) {
        this.language_id = language_id;
    }
}
