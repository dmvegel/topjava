package ru.javawebinar.topjava.repository.jdbc.converter;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.function.Function;

@Component
@Profile("hsqldb")
public class DateToTimestampConverter implements Function<LocalDateTime, Timestamp> {
    @Override
    public Timestamp apply(LocalDateTime localDateTime) {
        return Timestamp.valueOf(localDateTime);
    }
}
