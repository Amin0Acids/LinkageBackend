package com.Aminoacid.linkage.repository;

import com.Aminoacid.linkage.model.Session;
import com.Aminoacid.linkage.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SessionRepo extends JpaRepository<Session, Long> {
    Optional<List<Session>> findByOrganizer(User organizer);
}
