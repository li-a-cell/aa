package com.hlw.pojo;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "employee") // 保持表名为 'employee'
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "employee_id") // 映射到表中的 employee_id
    private Integer employee_id; // 员工ID（主键）

    @Column(nullable = false, unique = true)
    private String account; // 员工账号

    @Column(nullable = false)
    private String password; // 员工密码

    @Column(name = "phone_number")
    private String phone_number; // 手机号

    @Lob
    @Column(name = "profile_picture", length = 16777215) // 存储 BLOB 图片
    private byte[] profile_picture; // 头像，使用 BLOB 存储（存储为二进制数据）

    private String name; // 姓名

    private String gender; // 性别（字符串类型，数据库中的 ENUM 会映射为字符串）

    @Column(name = "birth_date")
    private LocalDate birth_date; // 出生年月

    private String address; // 家庭住址

  @Enumerated(EnumType.STRING)
    @Column(name = "job_type", nullable = false)
    private jobType job_type; // 职位类型（检查人员，招标人员，项目经理，后台管理员）

    @Column(name = "hire_date", updatable = false)
    private LocalDate hire_date; // 入职时间（使用 LocalDate）

    private Double salary; // 薪水
    public enum jobType {
        检查人员,招标人员,项目经理,后台管理员
    }
}
