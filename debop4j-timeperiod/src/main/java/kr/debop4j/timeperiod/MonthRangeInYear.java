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

package kr.debop4j.timeperiod;

import com.google.common.base.Objects;
import kr.debop4j.core.ValueObjectBase;
import kr.debop4j.core.tools.HashTool;
import lombok.Getter;

/**
 * 한해의 월단위 기간을 표현합니다. (예: 4월 ~ 6월)
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 5. 12. 오후 6:46
 */
public class MonthRangeInYear extends ValueObjectBase implements Comparable<MonthRangeInYear> {

    private static final long serialVersionUID = -1797303419172720812L;

    @Getter private final int startMonthOfYear;
    @Getter private final int endMonthOfYear;

    public MonthRangeInYear(int startMonthOfYear, int endMonthOfYear) {
        assert startMonthOfYear <= endMonthOfYear;
        this.startMonthOfYear = startMonthOfYear;
        this.endMonthOfYear = endMonthOfYear;
    }

    public boolean isSingleMonth() {
        return startMonthOfYear == endMonthOfYear;
    }

    public boolean hasInside(int monthOfYear) {
        return this.startMonthOfYear <= monthOfYear &&
                monthOfYear <= this.endMonthOfYear;
    }

    @Override
    public int compareTo(MonthRangeInYear o) {
        return this.startMonthOfYear - o.getStartMonthOfYear();
    }

    @Override
    public int hashCode() {
        return HashTool.compute(startMonthOfYear, endMonthOfYear);
    }

    @Override
    protected Objects.ToStringHelper buildStringHelper() {
        return super.buildStringHelper()
                .add("startMonthOfYear", startMonthOfYear)
                .add("endMonthOfYear", endMonthOfYear);
    }
}
