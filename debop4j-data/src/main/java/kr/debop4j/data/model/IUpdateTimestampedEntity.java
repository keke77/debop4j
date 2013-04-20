package kr.debop4j.data.model;

import java.util.Date;

/**
 * 최근 수정일자를 속성으로 가지고 있는 엔티티를 표현하는 인터페이스
 *
 * @author sunghyouk.bae@gmail.com
 * @since 12. 11. 21.
 */
public interface IUpdateTimestampedEntity {

    /**
     * 엔티티의 최근 갱신 일자를 반환합니다.
     */
    Date getUpdateTimestamp();

    /**
     * 엔티티의 최근 갱신 일자를 수정합니다.
     */
    void updateUpdateTimestamp();
}
