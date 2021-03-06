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

package kr.debop4j.timeperiod.test.timeranges;

import kr.debop4j.timeperiod.test.TimePeriodTestBase;
import kr.debop4j.timeperiod.timerange.DayRange;
import kr.debop4j.timeperiod.timerange.DayRangeCollection;
import kr.debop4j.timeperiod.tools.Times;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.junit.Test;

import java.util.List;

import static org.fest.assertions.Assertions.assertThat;

/**
 * kr.debop4j.timeperiod.test.timeranges.DayRangeCollectionTest
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 5. 25. 오후 3:15
 */
@Slf4j
public class DayRangeCollectionTest extends TimePeriodTestBase {

    @Test
    public void singleDays() {
        final DateTime start = Times.asDate(2004, 2, 22);
        DayRangeCollection days = new DayRangeCollection(start, 1);

        assertThat(days.getDayCount()).isEqualTo(1);

        assertThat(days.getStartYear()).isEqualTo(start.getYear());
        assertThat(days.getStartMonthOfYear()).isEqualTo(start.getMonthOfYear());
        assertThat(days.getStartDayOfMonth()).isEqualTo(start.getDayOfMonth());

        assertThat(days.getEndYear()).isEqualTo(start.getYear());
        assertThat(days.getEndMonthOfYear()).isEqualTo(start.getMonthOfYear());
        assertThat(days.getEndDayOfMonth()).isEqualTo(start.getDayOfMonth());

        List<DayRange> dayList = days.getDays();
        assertThat(dayList.size()).isEqualTo(1);
        assertThat(dayList.get(0).isSamePeriod(new DayRange(start))).isTrue();
    }

    @Test
    public void calendarDays() {
        final int dayCount = 5;

        final DateTime start = Times.asDate(2004, 2, 22);
        final DateTime end = start.plusDays(dayCount - 1);
        DayRangeCollection days = new DayRangeCollection(start, dayCount);

        assertThat(days.getDayCount()).isEqualTo(dayCount);

        assertThat(days.getStartYear()).isEqualTo(start.getYear());
        assertThat(days.getStartMonthOfYear()).isEqualTo(start.getMonthOfYear());
        assertThat(days.getStartDayOfMonth()).isEqualTo(start.getDayOfMonth());

        assertThat(days.getEndYear()).isEqualTo(end.getYear());
        assertThat(days.getEndMonthOfYear()).isEqualTo(end.getMonthOfYear());
        assertThat(days.getEndDayOfMonth()).isEqualTo(end.getDayOfMonth());

        List<DayRange> dayList = days.getDays();
        assertThat(dayList.size()).isEqualTo(dayCount);

        for (int i = 0; i < dayCount; i++) {
            assertThat(dayList.get(i).isSamePeriod(new DayRange(start.plusDays(i)))).isTrue();
        }
    }

    public void calendarHoursTest() {

        final int[] dayCounts = new int[] { 1, 6, 48, 180, 480 };

        for (int dayCount : dayCounts) {
            DateTime now = Times.now();
            DayRangeCollection days = new DayRangeCollection(now, dayCount);

            DateTime startTime = now.withTimeAtStartOfDay().plus(days.getTimeCalendar().getStartOffset());
            DateTime endTime = startTime.plusDays(dayCount).plus(days.getTimeCalendar().getEndOffset());

            assertThat(days.getStart()).isEqualTo(startTime);
            assertThat(days.getEnd()).isEqualTo(endTime);

            assertThat(days.getDayCount()).isEqualTo(dayCount);

            List<DayRange> items = days.getDays();
            assertThat(items.size()).isEqualTo(dayCount);

            for (int i = 0; i < items.size(); i++) {
                assertThat(items.get(i).getStart()).isEqualTo(startTime.plusDays(i));
                assertThat(items.get(i).getEnd()).isEqualTo(days.getTimeCalendar().mapEnd(startTime.plusDays(i + 1)));
                assertThat(items.get(i).isSamePeriod(new DayRange(days.getStart().plusDays(i)))).isTrue();
            }
        }
    }
}
