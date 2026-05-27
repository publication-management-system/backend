package com.pms.publicationmanagement.dto.shared;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PagedResponse<T> {
    private List<T> data;
    private Long totalElements;
    private Long totalPages;
    private Integer pageNumber;
}
