package kr.debop4j.data.mongodb.test.associations;

import kr.debop4j.data.ogm.test.associations.onetoone.OneToOneTest;
import org.hibernate.cfg.Configuration;
import org.hibernate.ogm.datastore.mongodb.AssociationStorage;
import org.hibernate.ogm.datastore.mongodb.Environment;

/**
 * kr.debop4j.data.mongodb.test.associations.OneToOneGlobalTest
 *
 * @author 배성혁 ( sunghyouk.bae@gmail.com )
 * @since 13. 4. 14. 오후 6:58
 */
public class OneToOneGlobalTest extends OneToOneTest {
    @Override
    protected void configure(Configuration cfg) {
        super.configure(cfg);
        cfg.setProperty(Environment.MONGODB_ASSOCIATIONS_STORE,
                        AssociationStorage.GLOBAL_COLLECTION.name());
    }
}
