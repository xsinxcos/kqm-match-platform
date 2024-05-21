package com.chaos.post.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author : wzq
 * @since : 2024-05-15 22:32
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EditLivePostDto {
    @NotBlank
    private String title;

    @NotBlank
    private String content;

    @NotNull
    private Long groupId;

    @NotNull
    private Integer isSignin;

    private Long teamId;
}
