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
package kr.debop4j.search.dao;

import kr.debop4j.core.Local;
import kr.debop4j.data.hibernate.unitofwork.UnitOfWorks;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.search.Query;
import org.hibernate.*;
import org.hibernate.search.FullTextQuery;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.Search;

import java.io.Serializable;
import java.util.List;

/**
 * hibernate-search 를 이용하여 엔티티를 관리하는 Data Access Object 입니다.
 *
 * @author sunghyouk.bae@gmail.com
 * @since 13. 4. 20. 오후 10:03
 */
@Slf4j
@SuppressWarnings("unchecked")
public class HibernateSearchDaoImpl {

    private static final String SESSION_KEY = "kr.debop4j.search.dao.HibernateSearchDao.Session";
    private static final String FULL_TEXT_SESSION_KEY = "kr.debop4j.search.dao.HibernateSearchDao.FullTextSession";
    private static final int BATCH_SIZE = 100;

    @Getter
    @Setter
    private int batchSize;

    private final SessionFactory sessionFactory;

    public HibernateSearchDaoImpl() {
        this(UnitOfWorks.getCurrentSessionFactory());
    }

    public HibernateSearchDaoImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public synchronized Session getSession() {
        Session session = (Session) Local.get(SESSION_KEY);
        if (session == null || !session.isOpen()) {
            session = sessionFactory.openSession();
            Local.put(SESSION_KEY, session);
            if (log.isDebugEnabled())
                log.debug("새로운 Session을 open 했습니다.");
        }
        return session;
    }

    public synchronized FullTextSession getFullTextSession() {
        FullTextSession fts = (FullTextSession) Local.get(FULL_TEXT_SESSION_KEY);

        if (fts == null || !fts.isOpen()) {
            fts = Search.getFullTextSession(getSession());
            Local.put(FULL_TEXT_SESSION_KEY, fts);

            if (log.isDebugEnabled())
                log.debug("Current Thread Context에 새로운 FullTextSession을 생성했습니다. threadName=[{}]",
                          Thread.currentThread().getName());
        }
        return fts;
    }

    public <T> List<T> find(Query luceneQuery, Class<T> clazz) {
        FullTextQuery ftq = getFullTextSession().createFullTextQuery(luceneQuery, clazz);

        return (List<T>) ftq.list();
    }

    /**
     * 해당 엔티티의 인덱스 정보만을 삭제합니다.
     */
    public void purge(Class<?> clazz, Serializable id) {
        getFullTextSession().purge(clazz, id);
    }

    /**
     * 지정된 수형의 인덱싱 정보를 삭제합니다. (DB의 엔티티 정보는 보존합니다.)
     */
    public void purgeAll(Class<?> clazz) {
        getFullTextSession().purgeAll(clazz);
    }

    /**
     * 엔티티를 인덱싱합니다.
     *
     * @param entity
     * @param <T>
     */
    public <T> void index(T entity) {
        getFullTextSession().index(entity);
    }

    /**
     * 지정된 수형의 모든 엔티티들을 재 인덱싱 합니다.
     */
    public void indexAll(Class<?> clazz, int batchSize) {
        if (log.isDebugEnabled())
            log.debug("수형[{}]의 모든 엔티티에 대해 재 인덱싱을 수행합니다...", clazz);

        if (batchSize < BATCH_SIZE)
            batchSize = BATCH_SIZE;

        FullTextSession fts = getFullTextSession();
        fts.setFlushMode(FlushMode.MANUAL);
        fts.setCacheMode(CacheMode.IGNORE);

        try {
            Transaction tx = fts.beginTransaction();
            ScrollableResults results = fts.createCriteria(clazz).scroll(ScrollMode.FORWARD_ONLY);
            int index = 0;
            while (results.next()) {
                index++;
                fts.index(results.get(0));
                if (index % batchSize == 0) {
                    fts.flushToIndexes();
                    fts.clear();
                }
            }
            fts.flushToIndexes();
            tx.commit();

            if (log.isDebugEnabled())
                log.debug("수형[{}]의 모든 엔티티 [{}]개 대해 재 인덱싱을 수행했습니다!!!", clazz, index);
        } finally {
            fts.setFlushMode(FlushMode.AUTO);
            fts.setCacheMode(CacheMode.NORMAL);
        }
    }
}
