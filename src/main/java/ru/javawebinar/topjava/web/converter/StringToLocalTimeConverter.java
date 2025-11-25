package ru.javawebinar.topjava.web.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;

import java.time.LocalTime;

import static ru.javawebinar.topjava.util.DateTimeUtil.parseLocalTime;

public class StringToLocalTimeConverter implements Converter<String, LocalTime> {
    @Override
    public LocalTime convert(@Nullable String source) {
        return parseLocalTime(source);
    }
}
