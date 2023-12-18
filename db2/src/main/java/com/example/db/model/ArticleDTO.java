package com.example.db.model;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class ArticleDTO {

    private Integer id;

    @Size(max = 255)
    private String name;

}
