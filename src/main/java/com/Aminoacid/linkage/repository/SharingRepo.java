package com.Aminoacid.linkage.repository;

import com.Aminoacid.linkage.model.Session;
import com.Aminoacid.linkage.model.Sharing;
import com.Aminoacid.linkage.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SharingRepo extends JpaRepository<Sharing, Long> {
    Optional<List<Sharing>> findBySession(Session session);
    Optional<List<Sharing>> findByReceiver(User receiver);

    Optional<Sharing> findBySessionAndReceiver(Session session, User user);

    void deleteAllBySession(Session session);
}
