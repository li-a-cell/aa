package com.hlw.pojo;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "project_bidding_record")
public class ProjectBiddingRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "record_id", nullable = false)
    private Integer recordId;


    @Column(name = "bidder_id")
    private int bidderId;

    @Column(name = "bidding_price", precision = 15, scale = 2)
    private BigDecimal biddingPrice;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "bidding_time")
    private LocalDateTime biddingTime;

}