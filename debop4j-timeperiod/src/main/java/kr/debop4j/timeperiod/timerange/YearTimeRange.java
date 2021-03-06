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

import com.google.common.collect.Lists;
import kr.debop4j.core.Guard;
import kr.debop4j.core.tools.HashTool;
import kr.debop4j.timeperiod.Halfyear;
import kr.debop4j.timeperiod.ITimeCalendar;
import kr.debop4j.timeperiod.Quarter;
import kr.debop4j.timeperiod.TimeRange;
import kr.debop4j.timeperiod.tools.TimeSpec;
import kr.debop4j.timeperiod.tools.Times;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;

import java.util.List;

/**
 * 년(Year) 단위의 기간을 표현합니다.
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 5. 13. 오후 11:48
 */
@Slf4j
public class YearTimeRange extends YearCalendarTimeRange {

    @Getter private final int yearCount;

    /**
     * Instantiates a new Year time range.
     *
     * @param moment    the moment
     * @param yearCount the year count
     * @param calendar  the calendar
     */
    public YearTimeRange(DateTime moment, int yearCount, ITimeCalendar calendar) {
        this(moment.getYear(), yearCount, calendar);
    }

    /**
     * Instantiates a new Year time range.
     *
     * @param startYear the start year
     * @param yearCount the year count
     * @param calendar  the calendar
     */
    public YearTimeRange(int startYear, int yearCount, ITimeCalendar calendar) {
        super(getPeriodOf(startYear, yearCount), calendar);
        Guard.shouldBePositiveNumber(yearCount, "yearCount");

        this.yearCount = yearCount;
    }

    /**
     * Gets halfyears.
     *
     * @return the halfyears
     */
    public List<HalfyearRange> getHalfyears() {
        int startYear = getStartYear();

        List<HalfyearRange> halfyears = Lists.newArrayListWithCapacity(yearCount * TimeSpec.HalfyearsPerYear);
        for (int y = 0; y < yearCount; y++) {
            halfyears.add(new HalfyearRange(startYear + y, Halfyear.First, getTimeCalendar()));
            halfyears.add(new HalfyearRange(startYear + y, Halfyear.Second, getTimeCalendar()));
        }
        return halfyears;
    }

    /**
     * Gets quarters.
     *
     * @return the quarters
     */
    public List<QuarterRange> getQuarters() {
        int startYear = getStartYear();

        List<QuarterRange> quarters = Lists.newArrayListWithCapacity(yearCount * TimeSpec.QuartersPerYear);
        Quarter[] quarterKinds = Quarter.values();
        for (int y = 0; y < yearCount; y++) {
            for (Quarter quarter : quarterKinds) {
                quarters.add(new QuarterRange(startYear + y, quarter, getTimeCalendar()));
            }
        }
        return quarters;
    }

    /**
     * Gets months.
     *
     * @return the months
     */
    public List<MonthRange> getMonths() {
        int startYear = getStartYear();
        List<MonthRange> months = Lists.newArrayListWithCapacity(yearCount * TimeSpec.MonthsPerYear);
        int monthCount = getYearCount() * TimeSpec.MonthsPerYear;

        DateTime startDate = Times.startTimeOfYear(getStartYear());

        for (int m = 0; m < monthCount; m++) {
            months.add(new MonthRange(startDate.plusMonths(m), getTimeCalendar()));
        }
        return months;
    }

    @Override
    public int hashCode() {
        return HashTool.compute(super.hashCode(), yearCount);
    }

    private static TimeRange getPeriodOf(int year, int yearCount) {
        assert yearCount > 0;

        DateTime startYear = Times.startTimeOfYear(year);
        return new TimeRange(startYear, startYear.plusYears(yearCount));
    }

    private static final long serialVersionUID = 1604523513628691621L;
}
