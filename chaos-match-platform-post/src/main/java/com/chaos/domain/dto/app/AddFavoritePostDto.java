package com.chaos.domain.dto.app;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * @description: addFavoritePostDto
 * @author: xsinxcos
 * @create: 2024-02-12 00:06
 **/
@AllArgsConstructor
@NoArgsConstructor
@Data
public class AddFavoritePostDto {

    @NotNull(message = "Id不能为NULL")
    private Long id;
}
