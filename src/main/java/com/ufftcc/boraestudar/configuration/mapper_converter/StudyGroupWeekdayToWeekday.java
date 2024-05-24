package com.ufftcc.boraestudar.configuration.mapper_converter;

import com.ufftcc.boraestudar.entities.StudyGroupWeekday;
import com.ufftcc.boraestudar.entities.Weekday;
import org.modelmapper.AbstractConverter;

public class StudyGroupWeekdayToWeekday extends AbstractConverter<StudyGroupWeekday, Weekday> {
    @Override
    protected Weekday convert(StudyGroupWeekday studyGroupWeekdays) {
        Weekday weekday = new Weekday();
        weekday.setId(studyGroupWeekdays.getWeekday().getId());
        weekday.setName(studyGroupWeekdays.getWeekday().getName());

        return weekday;
    }

}
