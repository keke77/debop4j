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
import com.google.common.collect.Sets;
import kr.debop4j.access.model.IActor;
import kr.debop4j.core.Guard;
import kr.debop4j.core.tools.HashTool;
import kr.debop4j.data.model.AnnotatedEntityBase;
import kr.debop4j.data.model.ITreeEntity;
import kr.debop4j.data.model.IUpdateTimestampedEntity;
import kr.debop4j.data.model.TreeNodePosition;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.*;
import org.joda.time.DateTime;

import javax.persistence.CascadeType;
import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Set;

/**
 * 부서 정보를 나타냅니다. (상위부서, 하위부서 등을 표현합니다)
 *
 * @author 배성혁 ( sunghyouk.bae@gmail.com )
 * @since 13. 3. 1.
 */
@Entity
@Table(name = "Department")
@Cache(region = "Organization", usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@org.hibernate.annotations.Table(appliesTo = "Department",
                                 indexes = @org.hibernate.annotations.Index(name = "ix_department_code",
                                                                            columnNames = { "CompanyId", "DepartmentCode" }))
@DynamicInsert
@DynamicUpdate
@Getter
@Setter
public class Department extends AnnotatedEntityBase implements IActor, ITreeEntity<Department>, IUpdateTimestampedEntity {

    private static final long serialVersionUID = 512869366829603899L;

    protected Department() {}

    public Department(Company company, String departmentCode) {
        this(company, departmentCode, departmentCode);
    }

    public Department(Company company, String departmentCode, String departmentName) {
        Guard.shouldNotBeNull(company, "company");
        Guard.shouldNotBeEmpty(departmentCode, "departmentCode");
        Guard.shouldNotBeEmpty(departmentName, "departmentName");

        this.company = company;
        this.code = departmentCode;
        this.name = departmentName;
    }

    @Id
    @GeneratedValue
    @Column(name = "DepartmentId")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CompanyId", nullable = false)
    @ForeignKey(name = "fk_department_company")
    private Company company;

    @Column(name = "DepartmentCode", nullable = false, length = 64)
    private String code;

    @Column(name = "DepartmentName", nullable = false, length = 128)
    private String name;

    @Column(name = "DepartmentEName", length = 128)
    private String enam;

    @Column(name = "IsActive")
    private Boolean active;

    @Column(name = "DepartmentDesc", length = 4000)
    private String description;

    @Column(name = "ExAttr", length = 4000)
    private String exAttr;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ParentId")
    @ForeignKey(name = "fk_department_parent")
    private Department parent;

    @Setter(AccessLevel.PROTECTED)
    @OneToMany(mappedBy = "parent", cascade = { CascadeType.ALL })
    @LazyCollection(LazyCollectionOption.EXTRA)
    private Set<Department> children = Sets.newLinkedHashSet();

    @Embedded
    private TreeNodePosition nodePosition = new TreeNodePosition();


    @Type(type = "kr.debop4j.data.hibernate.usertype.JodaDateTimeUserType")
    private DateTime updateTimestamp;

    @OneToMany(mappedBy = "department", cascade = { CascadeType.ALL })
    @LazyCollection(value = LazyCollectionOption.EXTRA)
    @Fetch(FetchMode.SELECT)
    private Set<DepartmentMember> members = Sets.newHashSet();

    @Override
    public void updateUpdateTimestamp() {
        updateTimestamp = DateTime.now();
    }

    @Override
    public int hashCode() {
        if (isPersisted())
            return super.hashCode();
        return HashTool.compute(company, code);
    }

    @Override
    protected Objects.ToStringHelper buildStringHelper() {
        return super.buildStringHelper()
                .add("id", id)
                .add("companyId", company.getId())
                .add("code", code)
                .add("name", name);
    }
}
