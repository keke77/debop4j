package kr.debop4j.data.ogm.test.datastore;

import kr.debop4j.data.ogm.model.UuidEntityBase;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;

/**
 * kr.debop4j.data.ogm.test.datastore.Noise
 *
 * @author 배성혁 ( sunghyouk.bae@gmail.com )
 * @since 13. 4. 1
 */
@Entity
@Getter
@Setter
public class Noise extends UuidEntityBase {

    private String frequency;
}
