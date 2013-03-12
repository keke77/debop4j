package kr.debop4j.access.model;

import kr.debop4j.access.AccessTestBase;
import kr.debop4j.data.hibernate.unitofwork.UnitOfWorks;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * kr.debop4j.access.model.MappingTest
 * User: sunghyouk.bae@gmail.com
 * Date: 13. 3. 7.
 */
@Slf4j
public class MappingTest extends AccessTestBase {

    @Test
    public void mappingTest() {
        UnitOfWorks.start();

        // Nothing to do.

        UnitOfWorks.stop();
    }
}
