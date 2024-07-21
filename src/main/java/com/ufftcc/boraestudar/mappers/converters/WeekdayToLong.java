package com.ufftcc.boraestudar.mappers.converters;

import com.ufftcc.boraestudar.entities.Weekday;
import org.modelmapper.AbstractConverter;

public class WeekdayToLong extends AbstractConverter<Weekday, Long> {
    @Override
    protected Long convert(Weekday weekday) {
        return weekday.getId();
    }
}
