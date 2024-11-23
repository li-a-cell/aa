package com.hlw.dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectNodeIdDto {
    private LocalDate start_date;  // 开始日期


    private LocalDate end_date;  // 结束日期
}
