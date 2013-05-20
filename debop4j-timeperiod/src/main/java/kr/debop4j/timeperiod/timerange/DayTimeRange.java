/*
 * Copyright 2011-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package kr.debop4j.timeperiod.timerange;

import com.google.common.base.Objects;
import com.google.common.collect.Lists;
import kr.debop4j.core.tools.HashTool;
import kr.debop4j.timeperiod.DayOfWeek;
import kr.debop4j.timeperiod.ITimeCalendar;
import kr.debop4j.timeperiod.TimeCalendar;
import kr.debop4j.timeperiod.TimeRange;
import kr.debop4j.timeperiod.tools.TimeSpec;
import lombok.Getter;
import org.joda.time.DateTime;

import java.util.List;

/**
 * 일(Day) 단위로 기간을 표현합니다.
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 5. 13. 오후 1:57
 */
public abstract class DayTimeRange extends CalendarTimeRange {

    private static final long serialVersionUID = 3989374355981125247L;

    // region << Constructor >>

    protected DayTimeRange(DateTime moment, int dayCount) {
        this(moment, dayCount, new TimeCalendar());
    }

    protected DayTimeRange(DateTime moment, int dayCount, ITimeCalendar calendar) {
        this(calendar.getYear(moment), calendar.getMonthOfYear(moment), calendar.getDayOfMonth(moment), dayCount, calendar);
    }

    protected DayTimeRange(int year, int monthOfYear, int dayOfMonth, int dayCount) {
        this(year, monthOfYear, dayOfMonth, dayCount, new TimeCalendar());
    }

    protected DayTimeRange(int year, int monthOfYear, int dayOfMonth, int dayCount, ITimeCalendar calendar) {
        super(getPeriodOf(year, monthOfYear, dayOfMonth, dayCount), calendar);
        this.dayCount = dayCount;
    }

    // endregion

    @Getter private final int dayCount;

    public DayOfWeek getStartDayOfWeek() {
        return getTimeCalendar().getDayOfWeek(getStart());
    }

    public String getStartDayName() {
        return getTimeCalendar().getDayName(getStartDayOfWeek());
    }

    public DayOfWeek getEndDayOfWeek() {
        return getTimeCalendar().getDayOfWeek(getEnd());
    }

    public String getEndDayName() {
        return getTimeCalendar().getDayName(getEndDayOfWeek());
    }

    /** 일(Day) 단위의 기간에 속한 시간 단위의 기간 정보 (<see cref="HourRange"/>)의 컬렉션을 반환합니다. */
    public List<HourRange> getHours() {
        DateTime startDay = getStartDayStart();

        List<HourRange> hours = Lists.newArrayListWithCapacity(dayCount * TimeSpec.HoursPerDay);
        for (int d = 0; d < dayCount; d++) {
            for (int h = 0; h < TimeSpec.HoursPerDay; h++) {
                HourRange hour = new HourRange(startDay.plusHours(d * TimeSpec.HoursPerDay + h), getTimeCalendar());
                hours.add(hour);
            }
        }
        return hours;
    }

    private static TimeRange getPeriodOf(int year, int month, int day, int dayCount) {
        assert dayCount >= 0;

        DateTime start = new DateTime(year, month, day, 0, 0);
        DateTime end = start.plusDays(dayCount);

        return new TimeRange(start, end);
    }

    @Override
    public int hashCode() {
        return HashTool.compute(super.hashCode(), dayCount);
    }

    @Override
    protected Objects.ToStringHelper buildStringHelper() {
        return super.buildStringHelper()
                .add("dayCount", dayCount);
    }
}