package com.hlw.pojo;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "bidder")
public class Bidder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bidder_id") // 映射到数据库中的 bidder_id
    private int bidderId; // 投标人ID

    @Column(name = "account", nullable = false, unique = true, length = 50)
    private String account; // 账号

    @Column(name = "password", nullable = false)
    private String password; // 密码

    @Column(name = "name", nullable = false, length = 100)
    private String name; // 姓名

    @Column(name = "phone_number", length = 20)
    private String phoneNumber; // 电话号码

    @Column(name = "company_name", length = 255)
    private String companyName; // 公司名称

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Status status; // 状态（黑名单/白名单）

    // 枚举类：状态
    public enum Status {
        黑名单, 白名单
    }
}
