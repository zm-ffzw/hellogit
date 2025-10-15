package com.sky.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Ticket implements Serializable {

    private static final long serialVersionUID = 1L;  // 序列化版本号

    private Long id;  // 工单ID

    private String content;  // 工单内容

    private String status;  // 工单状态（PENDING/处理中/已完成等）

    private LocalDateTime createTime;  // 创建时间

    // 新增：关联的员工ID（对应数据库employee_id）
    private Long employeeId;

    // 可选：若需要关联查询员工信息，可添加Employee对象（需配合MyBatis的关联查询）
    // private Employee employee;
}