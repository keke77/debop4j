package kr.debop4j.data.ehcache.ogm.test.id;

import kr.debop4j.data.ogm.test.id.SequenceIdGeneratorTest;
import lombok.extern.slf4j.Slf4j;
import org.junit.Ignore;

/**
 * kr.debop4j.data.ehcache.ogm.test.id.EhCacheSequenceIdGeneratorTest
 *
 * @author sunghyouk.bae@gmail.com
 * @since 13. 4. 10. 오후 8:29
 */
@Slf4j
@Ignore("JTA 에서만 제대로 작동한다.")
public class EhCacheSequenceIdGeneratorTest extends SequenceIdGeneratorTest {
}