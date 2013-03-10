package kr.debop4j.access.model.product;

import com.google.common.base.Objects;
import kr.debop4j.access.model.organization.Department;
import kr.debop4j.access.model.organization.Employee;
import kr.debop4j.access.model.organization.User;
import kr.debop4j.core.tools.HashTool;
import kr.debop4j.data.model.AnnotatedEntityBase;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.Date;
import java.util.Locale;

/**
 * 사용자의 활동 로그를 기록하빈다.
 * User: sunghyouk.bae@gmail.com
 * Date: 13. 3. 10.
 */
@Entity
@Table(name = "UserActivityLog")
@DynamicInsert
@DynamicUpdate
@Getter
@Setter
public class UserActivityLog extends AnnotatedEntityBase {

    private static final long serialVersionUID = -1677314855056130721L;

    public UserActivityLog() {}

    public UserActivityLog(String activityKind, Product product, User user) {

        this.activityKind = activityKind;
        this.activityTime = new Date();

        if (product != null) {
            this.productCode = product.getCode();
            this.productName = product.getName();
        }
        if (user != null) {
            this.username = user.getUsername();
            Employee emp = user.getEmployee();
            if (emp != null) {
                this.employeeCode = emp.getCode();
                this.employeeName = emp.getName();
                this.companyCode = emp.getCompany().getCode();
                this.companyName = emp.getCompany().getName();
            }
            Department dept = user.getDepartment();
            if (dept != null) {
                this.departmentCode = dept.getCode();
                this.departmentName = dept.getName();
            }
        }
        this.locale = Locale.getDefault();
    }

    @Id
    @GeneratedValue
    @Column(name = "LogId")
    private Long id;

    @Column(name = "ActiviyKind", nullable = false, length = 128)
    private String activityKind;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "ActivityTime")
    private Date activityTime;

    @Column(nullable = false, length = 128)
    private String productCode;

    private String productName;

    @Column(nullable = false, length = 128)
    private String companyCode;

    private String companyName;

    @Column(length = 128)
    private String departmentCode;

    private String departmentName;

    @Column(length = 128)
    private String employeeCode;

    private String employeeName;

    @Column(nullable = false, length = 128)
    private String username;

    private Locale locale;

    @Column(length = 2000)
    private String exAttr;


    @Override
    public int hashCode() {
        if (isPersisted())
            return HashTool.compute(id);

        return HashTool.compute(activityKind, activityTime, productCode, companyCode, username);
    }

    @Override
    protected Objects.ToStringHelper buildStringHelper() {
        return super.buildStringHelper()
                .add("id", id)
                .add("activityKind", activityKind)
                .add("activityTime", activityTime)
                .add("productCode", productCode)
                .add("productName", productName)
                .add("companyCode", companyCode)
                .add("companyName", companyName)
                .add("username", username);
    }
}
