package kr.debop4j.access.model.common;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 직책 (보직) 을 나타낸다. (개발본부장, TFT 팀장, 경영지원실장 등)
 * User: sunghyouk.bae@gmail.com
 * Date: 13. 3. 8 오후 5:10
 */
@Entity
@Table(name = "EmployeeTitle")
@DynamicInsert
@DynamicUpdate
@Getter
@Setter
public class EmployeeTitle extends EmployeeCodeBase {
}