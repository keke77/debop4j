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

package kr.debop4j.access.model.product;

import com.google.common.base.Objects;
import kr.debop4j.access.model.AccessEntityBase;
import kr.debop4j.core.Guard;
import kr.debop4j.core.tools.HashTool;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.Entity;
import java.util.Date;

/**
 * 사용자가 즐겨찾기한 컨텐츠 (메뉴)
 *
 * @author 배성혁 ( sunghyouk.bae@gmail.com )
 * @since 13. 3. 11.
 */
@Entity
@org.hibernate.annotations.Cache(region = "Product", usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@DynamicInsert
@DynamicUpdate
@Getter
@Setter
public class UserFavorite extends AccessEntityBase {

    private static final long serialVersionUID = 5857584976380701117L;

    protected UserFavorite() {}

    public UserFavorite(User user, String content) {
        Guard.shouldNotBeNull(user, "user");

        this.user = user;
        this.content = content;
    }

    @Id
    @GeneratedValue
    @Column(name = "UserFavoriteId")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "UserId", nullable = false)
    @ForeignKey(name = "fk_userfavorite_user")
    @Index(name = "ix_userfavorite")
    @NaturalId
    private User user;

    @Column(name = "Content", nullable = false, length = 2000)
    @NaturalId
    private String content;

    @Temporal(TemporalType.TIMESTAMP)
    private Date registDate;

    @Basic
    @Column(name = "IsActive")
    private Boolean active;

    /** 우선순위 */
    @Basic
    private Integer preference;

    @Column(length = 2000)
    private String description;

    @Basic(fetch = FetchType.LAZY)
    @Column(length = 2000)
    private String exAttr;

    @Override
    public int hashCode() {
        if (isPersisted())
            return HashTool.compute(id);
        return HashTool.compute(user, content);
    }

    @Override
    protected Objects.ToStringHelper buildStringHelper() {
        return super.buildStringHelper()
                .add("id", id)
                .add("user", user)
                .add("content", content)
                .add("registDate", registDate)
                .add("preference", preference)
                .add("active", active)
                .add("description", description)
                .add("exAttr", exAttr);
    }
}
