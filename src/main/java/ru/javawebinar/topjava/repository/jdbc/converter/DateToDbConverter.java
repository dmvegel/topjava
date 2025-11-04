package ru.javawebinar.topjava.repository.jdbc.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Nullable;
import java.time.LocalDateTime;
import java.util.function.Function;

@Component
public class DateToDbConverter {
    private final Function<LocalDateTime, ?> converter;

    @Autowired
    public DateToDbConverter(@Nullable Function<LocalDateTime, ?> converter) {
        this.converter = converter;
    }

    public Object convert(LocalDateTime localDateTime) {
        return converter == null ? localDateTime : converter.apply(localDateTime);
    }
}
