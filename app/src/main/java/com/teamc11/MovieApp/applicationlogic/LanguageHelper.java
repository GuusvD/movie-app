package com.teamc11.MovieApp.applicationlogic;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;

import java.util.Locale;

public class LanguageHelper {
    private Context language;
    public LanguageHelper(Context language) {
        this.language = language;
    }

    public void upDateResource(String code) {
        Locale locale = new Locale(code);
        Locale.setDefault(locale);
        Resources resources = language.getResources();
        Configuration configuration = resources.getConfiguration();;
        configuration.locale = locale;
        resources.updateConfiguration(configuration, resources.getDisplayMetrics());
    }
}
