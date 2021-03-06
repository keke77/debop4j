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
import kr.debop4j.core.Guard;
import kr.debop4j.core.Pair;
import kr.debop4j.core.ValueObjectBase;
import kr.debop4j.core.tools.HashTool;
import kr.debop4j.timeperiod.tools.TimeSpec;
import kr.debop4j.timeperiod.tools.Times;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.joda.time.Duration;

/**
 * 기간을 표현하는 기본 클래스입니다.
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 5. 11. 오후 12:12
 */
@Slf4j
public class TimePeriodBase extends ValueObjectBase implements ITimePeriod {

    private static final long serialVersionUID = -7255762434105570062L;

    // region << Constructors >>

    /** Instantiates a new Time period base. */
    protected TimePeriodBase() {
        this(TimeSpec.MinPeriodTime, TimeSpec.MaxPeriodTime, false);
    }

    /**
     * Instantiates a new Time period base.
     *
     * @param readonly the readonly
     */
    protected TimePeriodBase(boolean readonly) {
        this(TimeSpec.MinPeriodTime, TimeSpec.MaxPeriodTime, readonly);
    }

    /**
     * Instantiates a new Time period base.
     *
     * @param moment the moment
     */
    protected TimePeriodBase(DateTime moment) {
        this(moment, moment, false);
    }

    /**
     * Instantiates a new Time period base.
     *
     * @param moment   the moment
     * @param readonly the readonly
     */
    protected TimePeriodBase(DateTime moment, boolean readonly) {
        this(moment, moment, readonly);
    }

    /**
     * Instantiates a new Time period base.
     *
     * @param start the start
     * @param end   the end
     */
    protected TimePeriodBase(DateTime start, DateTime end) {
        this(start, end, false);
    }

    /**
     * Instantiates a new Time period base.
     *
     * @param start    the start
     * @param end      the end
     * @param readonly the readonly
     */
    protected TimePeriodBase(DateTime start, DateTime end, boolean readonly) {
        start = Guard.firstNotNull(start, TimeSpec.MinPeriodTime);
        end = Guard.firstNotNull(end, TimeSpec.MaxPeriodTime);
        Pair<DateTime, DateTime> result = Times.adjustPeriod(start, end);
        this.start = result.getV1();
        this.end = result.getV2();
        this.readonly = readonly;
    }

    /**
     * Instantiates a new Time period base.
     *
     * @param start    the start
     * @param duration the duration
     */
    protected TimePeriodBase(DateTime start, Duration duration) {
        this(start, duration, false);
    }

    /**
     * Instantiates a new Time period base.
     *
     * @param start    the start
     * @param duration the duration
     * @param readonly the readonly
     */
    protected TimePeriodBase(DateTime start, Duration duration, boolean readonly) {
        Pair<DateTime, Duration> result = Times.adjustPeriod(start, duration);
        this.start = result.getV1();
        setDuration(result.getV2());
        this.readonly = readonly;
    }

    /**
     * Instantiates a new Time period base.
     *
     * @param source the source
     */
    protected TimePeriodBase(ITimePeriod source) {
        Guard.shouldNotBeNull(source, "source");

        this.start = source.getStart();
        this.end = source.getEnd();
        this.readonly = source.isReadonly();
    }

    /**
     * Instantiates a new Time period base.
     *
     * @param source   the source
     * @param readonly the readonly
     */
    protected TimePeriodBase(ITimePeriod source, boolean readonly) {
        Guard.shouldNotBeNull(source, "source");

        this.start = source.getStart();
        this.end = source.getEnd();
        this.readonly = readonly;
    }

    // endregion

    /** The Start. */
    @Getter
    protected DateTime start;

    /** The End. */
    @Getter
    protected DateTime end;

    /** The Readonly. */
    @Getter
    @Setter(AccessLevel.PROTECTED)
    protected boolean readonly;

    /** 기간을 TimeSpan으료 표현, 기간이 정해지지 않았다면 <see cref="TimeSpec.MaxPeriodTime"/> 을 반환합니다. */
    @Override
    public Duration getDuration() {
        return new Duration(getStart(), getEnd());
    }

    /**
     * Sets duration.
     *
     * @param duration the duration
     */
    public void setDuration(Duration duration) {
        assert duration.getMillis() >= Duration.ZERO.getMillis() : "Duration의 크기가 0보다 크거나 같아야 합니다.";
        if (hasStart())
            end = start.plus(duration);
    }

    @Override
    public boolean hasStart() {
        return start != TimeSpec.MinPeriodTime;
    }

    @Override
    public boolean hasEnd() {
        return end != TimeSpec.MaxPeriodTime;
    }

    @Override
    public boolean hasPeriod() {
        return hasStart() && hasEnd();
    }

    @Override
    public boolean isMoment() {
        return Objects.equal(start, end);
    }

    @Override
    public boolean isAnytime() {
        return !hasStart() && !hasEnd();
    }

    @Override
    public void setup(DateTime ns, DateTime ne) {
        if (log.isTraceEnabled())
            log.trace("기간을 새로 설정합니다. newStart=[{}], newEnd=[{}]", ns, ne);

        ns = Guard.firstNotNull(ns, TimeSpec.MinPeriodTime);
        ne = Guard.firstNotNull(ne, TimeSpec.MaxPeriodTime);

        if (ns.compareTo(ne) < 0) {
            this.start = ns;
            this.end = ne;
        } else {
            this.start = ne;
            this.end = ns;
        }
    }

    /**
     * Copy i time period.
     *
     * @return the i time period
     */
    public ITimePeriod copy() {
        return copy(Duration.ZERO);
    }

    @Override
    public ITimePeriod copy(Duration offset) {
        if (log.isTraceEnabled())
            log.trace("기간 [{}]에 offset[{}]을 준 기간을 반환합니다...", this, offset);

        if (offset == Duration.ZERO)
            return new TimeRange(this);

        return new TimeRange(hasStart() ? start.plus(offset.getMillis()) : start,
                             hasEnd() ? end.plus(offset.getMillis()) : end,
                             readonly);
    }

    @Override
    public void move(Duration offset) {
        if (offset == Duration.ZERO)
            return;
        assertMutable();

        if (log.isTraceEnabled()) log.trace("기간[{}]을 offset[{}]만큼 이동합니다.", this, offset);

        if (hasStart())
            start = start.plus(offset.getMillis());
        if (hasEnd())
            end = end.plus(offset.getMillis());
    }

    /** 시작과 완료 시각이 같은지 여부 */
    @Override
    public boolean isSamePeriod(ITimePeriod other) {
        return (other != null) &&
                Objects.equal(start, other.getStart()) &&
                Objects.equal(end, other.getEnd());
    }

    @Override
    public boolean hasInside(DateTime moment) {
        return Times.hasInside(this, moment);
    }

    @Override
    public boolean hasInside(ITimePeriod other) {
        return Times.hasInside(this, other);
    }

    @Override
    public boolean intersectsWith(ITimePeriod other) {
        return Times.intersectsWith(this, other);
    }

    @Override
    public boolean overlapsWith(ITimePeriod other) {
        return Times.overlapsWith(this, other);
    }

    @Override
    public void reset() {
        assertMutable();
        start = TimeSpec.MinPeriodTime;
        end = TimeSpec.MaxPeriodTime;
        if (log.isTraceEnabled())
            log.trace("기간을 리셋했습니다. start=[{}], end=[{}]", start, end);
    }

    @Override
    public PeriodRelation getRelation(ITimePeriod other) {
        return Times.getRelation(this, other);
    }

    @Override
    public ITimePeriod getIntersection(ITimePeriod other) {
        return Times.getIntersectionRange(this, other);
    }

    @Override
    public ITimePeriod getUnion(ITimePeriod other) {
        return Times.getUnionRange(this, other);
    }

    /** Assert mutable. */
    protected final void assertMutable() {
        assert !readonly : "readonly 입니다.";
    }

    @Override
    public int compareTo(ITimePeriod o) {
        return start.compareTo(o.getStart());
    }

    @Override
    public boolean equals(Object obj) {
        return obj != null &&
                getClass().equals(obj.getClass()) &&
                hashCode() == obj.hashCode();
    }

    @Override
    public int hashCode() {
        return HashTool.compute(start, end, readonly);
    }

    @Override
    protected Objects.ToStringHelper buildStringHelper() {
        return super.buildStringHelper()
                .add("start", start)
                .add("end", end);
        //.add("readonly", readonly);
    }
}
