package kr.debop4j.access.model;

import com.google.common.base.Objects;
import kr.debop4j.core.Guard;
import kr.debop4j.core.tools.HashTool;
import kr.debop4j.data.model.AnnotatedTreeEntityBase;
import kr.debop4j.data.model.IUpdateTimestampedEntity;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Index;

import javax.persistence.*;
import java.util.Date;

/**
 * 부서 정보를 나타냅니다. (상위부서, 하위부서 등을 표현합니다)
 * User: sunghyouk.bae@gmail.com
 * Date: 13. 3. 1.
 */
@Entity
@Table(name = "Department")
@DynamicInsert
@DynamicUpdate
@Getter
@Setter
public class Department extends AnnotatedTreeEntityBase<Department>
        implements ICodeBaseEntity, IUpdateTimestampedEntity {

    private static final long serialVersionUID = -2198558891376603272L;

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

    @ManyToOne
    @JoinColumn(name = "CompanyId", nullable = false)
    @Index(name = "ix_department_code") //, columnNames = {"CompanyId", "DepartmentCode", "DepartmentName"})
    private Company company;

    @Column(name = "DepartmentCode", nullable = false, length = 64)
    @Index(name = "ix_department_code")
    private String code;

    @Column(name = "DepartmentName", nullable = false, length = 128)
    @Index(name = "ix_department_code")
    private String name;

    @Column(name = "DepartmentEName", length = 128)
    private String enam;

    @Column(name = "IsActive")
    private Boolean active;

    @Column(name = "DepartmentDesc", length = 4000)
    private String description;

    @Column(name = "ExAttr", length = 4000)
    private String exAttr;

    @Temporal(TemporalType.TIMESTAMP)
    private Date updateTimestamp;

    @Override
    public void updateUpdateTimestamp() {
        updateTimestamp = new Date();
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
