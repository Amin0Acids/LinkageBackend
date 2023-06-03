package com.Aminoacid.linkage.repository;

import com.Aminoacid.linkage.model.Question;
import com.Aminoacid.linkage.model.Session;
import com.Aminoacid.linkage.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface QuestionRepo extends JpaRepository<Question, Long>{
    Optional<List<Question>> findBySession(Session session);

    Optional<List<Question>> findByUser(User user);

    Optional<List<Question>> findBySessionAndUser(Session session, User user);
}
