package com.pms.publicationmanagement.dto.graph;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class NodeDto {
    private String id;

    private String name;

    private String type;
}
