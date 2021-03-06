package kr.debop4j.search.hibernate;

import kr.debop4j.search.QueryMethod;
import kr.debop4j.search.SearchParameter;
import kr.debop4j.search.hibernate.model.SearchItem;
import kr.debop4j.search.tools.SearchTool;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.search.Query;
import org.junit.Test;

import java.util.List;

import static org.fest.assertions.Assertions.assertThat;

/**
 * kr.debop4j.search.hibernate.HibernateSearchTest
 *
 * @author 배성혁 ( sunghyouk.bae@gmail.com )
 * @since 13. 2. 28.
 */
@Slf4j
public class HibernateSearchTest extends SearchTestBase {

    private static final String str = "ASP.NET 웹 어플리케이션은 어플리케이션 Lifecycle, Page의 Lifecycle 에 상세한 event 를 정의하고 있어, event handler를 정의하면, 여러가지 선처리나 후처리를 수행할 수 있습니다.\n" +
            "Spring MVC 에서는 어떻게 하나 봤더니 Controller 에 Interceptor 를 등록하면 되더군요.\n" +
            "단계를 요약하자면...\n" +
            "org.springframework.web.servlet.HandlerInterceptor 또는 org.springframework.web.servlet.handler.HandlerInterceptorAdapter 를 상속받아 preHandler, postHandler, afterComletion 등에 원하는 작업을 구현합니다.\n" +
            "servlet.xml 에 위에서 작성한 Interceptor 를 등록합니다.\n" +
            "아주 쉽죠?\n" +
            "그럼 실제 예제와 함께 보시죠. 예제는 Spring Framework 3.2.1.RELEASE 와 Hibernate 4.1.9 Final 로 제작했습니다.\n" +
            "UnitOfWorkInterceptor 는 사용자 요청이 있으면 Start 하고, 요청 작업이 완료되면 Close 하도록 합니다. 이는 Hibernate 를 이용하여 Unit Of Work 패턴을 구현하여, 하나의 요청 중에 모든 작업을 하나의 Transaction으로 묶을 수 있고, 웹 개발자에게는 Unit Of Work 자체를 사용하기만 하면 되고, 실제 Lifecycle 은 Spring MVC 에서 관리하도록 하기 위해서입니다.";

    @Test
    @SuppressWarnings("unchecked")
    public void firstSearch() throws Exception {

        fts.purgeAll(SearchItem.class);

        for (int i = 0; i < 100; i++) {
            SearchItem item = new SearchItem();
            item.setTitle("Spring MVC 전후 처리기 작성하기" + i);
            item.setDescription(str);
            item.setEan("USD");
            item.setImageURL("http://debop.blogspot.com/id=" + i);

            fts.save(item);
        }
        fts.flush();
        fts.flushToIndexes();
        fts.clear();

        try {
            // Query luceneQuery = parser.parse("description:어플");
            // Query luceneQuery = new WildcardQuery(new Term("description", "어플*"));
            SearchParameter searchParam = new SearchParameter("description", "app", QueryMethod.Wildcard);
            Query luceneQuery = SearchTool.buildLuceneQuery(SearchItem.class, fts, searchParam).all().createQuery();

            List<SearchItem> founds =
                    (List<SearchItem>) fts.createFullTextQuery(luceneQuery, SearchItem.class).list();

            assertThat(founds).isNotNull();
            assertThat(founds.size()).isGreaterThan(0);

            for (SearchItem loaded : founds)
                System.out.printf("Id=%d; Title: %s\n", loaded.getId(), loaded.getTitle());

        } catch (Exception e) {
            log.error("예외가 발생했습니다.", e);
        }

        for (Object element : fts.createQuery("from " + SearchItem.class.getName()).list()) {
            fts.delete(element);
        }
    }
}