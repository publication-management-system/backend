package com.pms.publicationmanagement.dto.stats;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DocumentByYear {
    private String year;
    private Long documentCount;
}
