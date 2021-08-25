package org.zerock.club2.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.zerock.club2.entity.Note;

import java.util.List;
import java.util.Optional;

public interface NoteRepository extends JpaRepository<Note, Long> {

    @EntityGraph(attributePaths = "writer", type = EntityGraph.EntityGraphType.LOAD)
    @Query("select n from Note n where n.num = :num")
    Optional<Note> getWithWriter(Long num);

    @EntityGraph(attributePaths = {"writer"} , type = EntityGraph.EntityGraphType.LOAD)
    @Query("select n from Note n where n.wirter.email = :email")
    List<Note> getList(String email);


}
