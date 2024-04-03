package com.chaos.domain.dto.app;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * @description: DeleteFavoritePostDto
 * @author: xsinxcos
 * @create: 2024-02-12 00:36
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeleteFavoritePostDto {

    @NotNull(message = "Id不能为NULL")
    private Long id;
}
