package ru.sber.models.kafka_models;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PageModel implements Serializable {
    private Integer page;
    private Integer pageSize;
}
