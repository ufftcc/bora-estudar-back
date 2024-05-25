package com.ufftcc.boraestudar.configuration.mapper_converter;

import com.ufftcc.boraestudar.entities.StudyGroupWeekday;
import com.ufftcc.boraestudar.entities.Weekday;
import org.modelmapper.AbstractConverter;

public class WeekdayToLong extends AbstractConverter<Weekday, Long> {
    @Override
    protected Long convert(Weekday weekday) {
        return weekday.getId();
    }
}
