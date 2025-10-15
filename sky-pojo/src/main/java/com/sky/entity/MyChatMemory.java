package com.sky.entity;

import java.time.LocalDateTime;
import java.io.Serializable;

import lombok.*;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author author
 * @since 2025-10-11
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MyChatMemory implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private String sessionId;

    private String role;

    private String content;

    private LocalDateTime createdAt;

}
