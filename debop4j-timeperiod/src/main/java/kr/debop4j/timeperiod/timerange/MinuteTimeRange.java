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

import kr.debop4j.core.tools.HashTool;
import kr.debop4j.timeperiod.ITimeCalendar;
import kr.debop4j.timeperiod.ITimePeriod;
import kr.debop4j.timeperiod.TimeCalendar;
import kr.debop4j.timeperiod.TimeRange;
import kr.debop4j.timeperiod.tools.TimeSpec;
import lombok.Getter;
import org.joda.time.DateTime;
import org.joda.time.Duration;

/**
 * 분(Minute) 단위로 기간을 표현합니다.
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 5. 12. 오후 9:51
 */
public class MinuteTimeRange extends CalendarTimeRange {
    private static final long serialVersionUID = -5669915582907325590L;

    protected MinuteTimeRange(DateTime moment, int minuteCount) {
        this(moment, minuteCount, new TimeCalendar());
    }

    protected MinuteTimeRange(DateTime moment, int minuteCount, ITimeCalendar calendar) {
        this(calendar.getYear(moment), calendar.getMonth(moment), calendar.getDayOfMonth(moment), calendar.getHour(moment), calendar.getMinute(moment), minuteCount, calendar);
    }

    protected MinuteTimeRange(int year, int monthOfYear, int dayOfMonth, int hourOfDay, int minuteOfHour, int minuteCount) {
        this(year, monthOfYear, dayOfMonth, hourOfDay, minuteOfHour, minuteCount, new TimeCalendar());
    }

    protected MinuteTimeRange(int year, int monthOfYear, int dayOfMonth, int hourOfDay, int minuteOfHour, int minuteCount, ITimeCalendar calendar) {
        super(getPeriodOf(year, monthOfYear, dayOfMonth, hourOfDay, minuteOfHour, minuteCount), calendar);

        this.minuteCount = minuteCount;
        this.endMinute = getStart().plusMinutes(minuteCount).getMinuteOfHour();
    }

    @Getter private final int minuteCount;
    @Getter private final int endMinute;

    @Override
    public int hashCode() {
        return HashTool.compute(super.hashCode(), endMinute);
    }

    private static ITimePeriod getPeriodOf(int year, int monthOfYear, int dayOfMonth, int hourOfDay, int minuteOfHour, int minuteCount) {
        DateTime start = new DateTime(year, monthOfYear, dayOfMonth, hourOfDay, minuteOfHour, 0);
        return new TimeRange(start, Duration.millis(minuteCount * TimeSpec.MillisPerMinute));
    }

}
