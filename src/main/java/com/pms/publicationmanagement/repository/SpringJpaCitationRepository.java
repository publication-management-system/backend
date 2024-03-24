package com.pms.publicationmanagement.repository;

import com.pms.publicationmanagement.model.Citation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SpringJpaCitationRepository extends JpaRepository<Citation, Integer> {


}
