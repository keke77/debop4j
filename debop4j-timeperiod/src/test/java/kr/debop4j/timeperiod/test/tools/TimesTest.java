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

package kr.debop4j.timeperiod.test.tools;

import kr.debop4j.timeperiod.ITimePeriod;
import kr.debop4j.timeperiod.TimeRange;
import kr.debop4j.timeperiod.test.TimePeriodTestBase;
import kr.debop4j.timeperiod.tools.Times;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;

/**
 * kr.debop4j.timeperiod.test.tools.TimesTest
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 5. 21. 오후 6:06
 */
@Slf4j
public class TimesTest extends TimePeriodTestBase {

    @Test
    public void asStringTest() {
        ITimePeriod period = new TimeRange(testDate, testNow);
        String periodString = Times.asString(period);

        log.debug("periodString=[{}]", periodString);
        assertThat(periodString).isNotEmpty();
    }

    @Test
    public void toDateTimeTest() {
        String dateString = testDate.toString();
        log.debug("dateString=[{}]", dateString);

        DateTime parsedTime = Times.toDateTime(dateString);

        assertThat(parsedTime.isEqual(testDate)).isTrue();

        parsedTime = Times.toDateTime("", testNow);
        assertThat(parsedTime).isEqualTo(testNow);
    }
}
