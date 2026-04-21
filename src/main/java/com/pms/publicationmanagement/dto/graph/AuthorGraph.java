package com.pms.publicationmanagement.dto.graph;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AuthorGraph {
    private List<NodeDto> nodes;

    private List<LinkDto> links;
}
