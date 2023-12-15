package ru.sber.models.kafka_models;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageModel implements Serializable {
    private Integer page;
    private Integer pageSize;
}
