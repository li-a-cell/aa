package com.hlw.pojo;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "project_bidding_record")
public class ProjectBiddingRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "record_id", nullable = false)
    private Integer record_id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bidder_id")
    private Bidder bidder_id;

    @Column(name = "bidding_price", precision = 15, scale = 2)
    private BigDecimal bidding_price;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "bidding_time")
    private Instant biddingTime;

}