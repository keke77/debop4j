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

package kr.debop4j.data.jpa.repository;

import kr.debop4j.core.tools.ReflectTool;
import kr.debop4j.data.jpa.domain.JpaEntityBase;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.Serializable;

/**
 * JPA 에서 사용할 Repository 입니다. <br/>
 * 참고: http://www.baeldung.com/2011/12/13/the-persistencexml-layer-with-spring-3-1-and-annotated/
 *
 * @author 배성혁 ( sunghyouk.bae@gmail.com )
 * @since 12. 11. 25.
 */
@Repository
@Slf4j
public abstract class JpaRepositoryBase<E extends JpaEntityBase> implements JpaRepository {

    @PersistenceContext
    private EntityManager em;

    @Getter
    private Class<E> entityType;

    public JpaRepositoryBase() {
        this.entityType = ReflectTool.getGenericParameterType(this);
    }

    public JpaRepositoryBase(Class<E> entityType) {
        this.entityType = entityType;
    }

    public void setEm(EntityManager em) {
        this.em = em;
    }

    public E get(Serializable id) {
        return em.getReference(entityType, id);
    }

    public E find(Serializable id) {
        return em.find(entityType, id);
    }

}
