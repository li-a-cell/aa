
package com.hlw.dto;

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


    private Integer recordId;

    private String projectName;

    private int projectId;

    private String bidderName;

    private int bidderId;

    private BigDecimal biddingPrice;

    private LocalDateTime biddingTime;

}
