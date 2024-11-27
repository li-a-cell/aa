
package com.hlw.pojo;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Getter
@Setter

public class ProjectBiddingRecordDto {


    private Integer record_id;

    private String project_name;

    private int project_id;

    private int bidder_name;

    private int bidder_id;

    private BigDecimal bidding_price;

    private LocalDateTime bidding_time;

}
