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
import kr.debop4j.access.model.ICodeBasedEntity;
import kr.debop4j.core.Guard;
import kr.debop4j.core.tools.HashTool;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 제품을 나타냅니다. 제품별로 사용자 관리, 권한 관리를 수행하도록 합니다.
 *
 * @author 배성혁 ( sunghyouk.bae@gmail.com )
 * @since 13. 3. 10.
 */
@Entity
@Table(name = "Product")
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@DynamicInsert
@DynamicUpdate
@Getter
@Setter
public class Product extends AccessEntityBase implements ICodeBasedEntity {

    private static final long serialVersionUID = -3955160861102847816L;

    protected Product() {}

    public Product(String code) {
        this(code, code);
    }

    public Product(String code, String name) {
        Guard.shouldNotBeEmpty(code, "code");
        Guard.shouldNotBeEmpty(name, "name");
        this.code = code;
        this.name = name;
    }

    @Id
    @GeneratedValue
    @Column(name = "ProductId")
    private Long id;

    @Column(name = "ProductCode", nullable = false, length = 128)
    @Index(name = "ix_product_code")
    @NaturalId(mutable = true)
    private String code;

    @Column(name = "ProductName", nullable = false, length = 255)
    @Index(name = "ix_product_code")
    private String name;

    @Column(name = "ProductAbbrName", nullable = false, length = 255)
    private String abbrName;

    @Column(name = "ProductActive")
    private Boolean active;

    @Column(name = "ProductDesc", length = 4000)
    private String description;

    @Column(name = "ProductExAttr", length = 4000)
    private String exAttr;

    @Override
    public int hashCode() {
        if (isPersisted())
            return HashTool.compute(id);
        return HashTool.compute(code);
    }

    @Override
    protected Objects.ToStringHelper buildStringHelper() {
        return super.buildStringHelper()
                .add("id", id)
                .add("code", code)
                .add("name", name)
                .add("abbrName", abbrName)
                .add("active", active);
    }
}
