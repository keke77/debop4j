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

package kr.debop4j.access.model.organization;

import com.google.common.base.Objects;
import com.google.common.collect.Maps;
import kr.debop4j.access.model.AccessLocaledEntityBase;
import kr.debop4j.access.model.IActor;
import kr.debop4j.core.Guard;
import kr.debop4j.core.ValueObjectBase;
import kr.debop4j.core.tools.HashTool;
import kr.debop4j.data.model.ILocaleValue;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Locale;
import java.util.Map;

/**
 * 회사 정보
 *
 * @author 배성혁 ( sunghyouk.bae@gmail.com )
 * @since 13. 3. 1.
 */
@Entity
@Table(name = "Company")
@Cache(region = "Organization", usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@org.hibernate.annotations.Table(appliesTo = "Company",
                                 indexes = @org.hibernate.annotations.Index(name = "ix_company_code",
                                                                            columnNames = { "CompanyCode", "CompanyName" }))
@DynamicInsert
@DynamicUpdate
@Getter
@Setter
public class Company extends AccessLocaledEntityBase<Company.CompanyLocale> implements IActor {

    private static final long serialVersionUID = -7337020664879632947L;

    protected Company() {}

    public Company(String companyCode) {
        this(companyCode, companyCode);
    }

    public Company(String companyCode, String companyName) {
        Guard.shouldNotBeEmpty(companyCode, "companyCode");
        Guard.shouldNotBeEmpty(companyName, "companyName");

        this.code = companyCode;
        this.name = companyName;
        this.active = true;
    }

    @Id
    @GeneratedValue
    @Column(name = "CompanyId")
    @Setter(AccessLevel.PROTECTED)
    private Long id;

    @Column(name = "CompanyCode", nullable = false, length = 128)
    @Index(name = "ix_company_code")
    private String code;

    @Column(name = "CompanyName", nullable = false, length = 128)
    @Index(name = "ix_company_code")
    private String name;

    @Column(name = "CompanyEName", length = 128)
    private String ename;

    @Basic
    @Column(name = "IsActive")
    private Boolean active;

    @Basic
    @Column(name = "CompanyDesc", length = 2000)
    private String description;

    @Basic(fetch = FetchType.LAZY)
    @Column(name = "ExAttr", length = 2000)
    private String exAttr;

    /** 다국어 지원을 위한 정보 */
    @CollectionTable(name = "CompanyLocale", joinColumns = { @JoinColumn(name = "CompanyId") })
    @ElementCollection(targetClass = CompanyLocale.class, fetch = FetchType.LAZY)
    @MapKeyClass(Locale.class)
    @Cascade({ org.hibernate.annotations.CascadeType.ALL })
    @LazyCollection(LazyCollectionOption.EXTRA)
    private Map<Locale, CompanyLocale> localeMap = Maps.newHashMap();

    public Map<Locale, CompanyLocale> getLocaleMap() {
        return localeMap;
    }

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
                .add("active", active)
                .add("description", description);
    }

    @Getter
    @Setter
    @Embeddable
    @DynamicInsert
    @DynamicUpdate
    public static class CompanyLocale extends ValueObjectBase implements ILocaleValue {

        public CompanyLocale() {}

        public CompanyLocale(String name, String description) {
            this.name = name;
            this.description = description;
        }

        @Column(name = "CompanyName", length = 128)
        private String name;

        @Basic
        @Column(name = "CompanyDesc", length = 2000)
        private String description;

        @Override
        public int hashCode() {
            return HashTool.compute(name, description);
        }

        @Override
        protected Objects.ToStringHelper buildStringHelper() {
            return super.buildStringHelper()
                    .add("name", name)
                    .add("description", description);
        }

        private static final long serialVersionUID = 3403174284080835688L;
    }
}
