package com.sky.entity;

import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

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
public class ChatMemoryStory implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private String memoryId;

    private String content;

    private LocalDateTime createdAt;

}
