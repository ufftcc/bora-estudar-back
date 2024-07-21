package com.ufftcc.boraestudar.mappers.converters;

import com.ufftcc.boraestudar.entities.StudyGroupWeekday;
import com.ufftcc.boraestudar.entities.Weekday;
import org.modelmapper.AbstractConverter;

public class WeekdayToStudyGroupWeekday extends AbstractConverter<Weekday, StudyGroupWeekday> {
    @Override
    protected StudyGroupWeekday convert(Weekday weekday) {
        StudyGroupWeekday studyGroupWeekday = new StudyGroupWeekday();
        studyGroupWeekday.setWeekday(weekday);

        return studyGroupWeekday;
    }
}
