package com.example.board.config.locale;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class LocaleTranslator {
    private static ResourceBundleMessageSource resourceBundleMessageSource;

    /*@Value("${app.locale.logs}")
    private static String lang;*/
    private static Locale logsLocale/* = new Locale(lang)*/;


    public LocaleTranslator(@Qualifier("texts") ResourceBundleMessageSource resourceBundleMessageSource, @Value("${app.locale.logs}") String logsLang) {
        this.resourceBundleMessageSource = resourceBundleMessageSource;
        logsLocale = new Locale(logsLang);
    }

    public static String translate(String messageCode, Object... params) {
        return resourceBundleMessageSource.getMessage(messageCode, params, LocaleContextHolder.getLocale());
    }

    public static String translateForLogs(String messageCode, Object... params) {
        return resourceBundleMessageSource.getMessage(messageCode, params, logsLocale);
    }
}
