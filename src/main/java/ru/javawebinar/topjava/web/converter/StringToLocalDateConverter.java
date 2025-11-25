package ru.javawebinar.topjava.web.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;

import java.time.LocalDate;

import static ru.javawebinar.topjava.util.DateTimeUtil.parseLocalDate;

public class StringToLocalDateConverter implements Converter<String, LocalDate> {
    @Override
    public LocalDate convert(@Nullable String source) {
        return parseLocalDate(source);
    }
}
