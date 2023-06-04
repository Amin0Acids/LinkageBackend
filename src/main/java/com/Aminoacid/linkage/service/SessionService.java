package com.Aminoacid.linkage.service;

import com.Aminoacid.linkage.dao.*;
import com.Aminoacid.linkage.model.Question;
import com.Aminoacid.linkage.model.Roles;
import com.Aminoacid.linkage.model.Session;
import com.Aminoacid.linkage.model.Sharing;
import com.Aminoacid.linkage.repository.QuestionRepo;
import com.Aminoacid.linkage.repository.SessionRepo;
import com.Aminoacid.linkage.repository.SharingRepo;
import com.Aminoacid.linkage.repository.UserRepo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class SessionService {

    private final SessionRepo sessionRepo;
    private final UserRepo userRepo;
    private final QuestionRepo questionRepo;
    private final SharingRepo sharingRepo;
    private final JWTService jwtService;

    public SessionResponse createSession(String token, String slide) {
        var user = jwtService.extractBodyUsername(token);
        var organizer = userRepo.findByUsername(user).orElseThrow();
        if (organizer.getRole() == Roles.STUDENT){
            throw new RuntimeException("You do not have permission to create a session");
        } else {
            var session = sessionRepo.save(
                    Session.builder()
                            .organizer(organizer)
                            .slide(slide)
                            .build()
            );
            return SessionResponse.builder()
                    .isSuccessful(true)
                    .id(session.getId())
                    .build();
        }
    }

    public StateResponse addParticipant(String token, ParticipantRequest request){
        var user = jwtService.extractBodyUsername(token);
        var session = sessionRepo.findById(request.getSessionID()).orElseThrow();
        if (!session.getOrganizer().getUsername().equals(user)) {
            throw new RuntimeException("You are not the organizer of this session");
        } else {
            Sharing invite = Sharing.builder()
                    .session(session)
                    .receiver(userRepo.findByUsername(request.getParticipantUsername()).orElseThrow())
                    .build();
            sharingRepo.save(invite);
        }
        return StateResponse.builder()
                .isSuccessful(true)
                .build();
    }

    public StateResponse deleteParticipant(String token, ParticipantRequest request){
        var user = jwtService.extractBodyUsername(token);
        var session = sessionRepo.findById(request.getSessionID()).orElseThrow();
        if (!session.getOrganizer().getUsername().equals(user)) {
            throw new RuntimeException("You are not the organizer of this session");
        } else {
            Sharing invite = sharingRepo.findBySessionAndReceiver(session, userRepo.findByUsername(request.getParticipantUsername()).orElseThrow()).orElseThrow();
            sharingRepo.delete(invite);
        }
        return StateResponse.builder()
                .isSuccessful(true)
                .build();
    }

    public GetParticipantResponse getAllParticipants(String token, Long sessionId){
        var user = jwtService.extractBodyUsername(token);
        var session = sessionRepo.findById(sessionId).orElseThrow();
        if (!session.getOrganizer().getUsername().equals(user)) {
            throw new RuntimeException("You are not the organizer of this session");
        } else {
            List<Sharing> invites = sharingRepo.findBySession(session).orElseThrow();
            List<String> participants = new ArrayList<>();
            for (Sharing invite : invites) {
                participants.add(invite.getReceiver().getUsername());
            }
            return GetParticipantResponse.builder()
                    .usernames(participants)
                    .build();
        }
    }

    public GetInviteResponse getAllInvites(String token){
        var user = jwtService.extractBodyUsername(token);
        var receiver = userRepo.findByUsername(user).orElseThrow();
        List<Sharing> invites = sharingRepo.findByReceiver(receiver).orElseThrow();
        List<GetInviteSingleResponse> inviteResponses = new ArrayList<>();
        for (Sharing invite : invites) {
            inviteResponses.add(
                    GetInviteSingleResponse.builder()
                            .sessionId(invite.getSession().getId())
                            .username(invite.getSession().getOrganizer().getUsername())
                            .build()
            );
        }
        return GetInviteResponse.builder()
                .invites(inviteResponses)
                .build();
    }

    public GetSessionSingleResponse getSession(String token, Long sessionId) {
        var user = jwtService.extractBodyUsername(token);
        var session = sessionRepo.findById(sessionId).orElseThrow();
        if (!session.getOrganizer().getUsername().equals(user)) {
            throw new RuntimeException("You are not the organizer of this session");
        } else {
            return GetSessionSingleResponse.builder()
                    .sessionId(session.getId())
                    .slide(session.getSlide())
                    .build();
        }
    }

    public GetSessionResponse getAllSessions(String token) {
        var user = jwtService.extractBodyUsername(token);
        var organizer = userRepo.findByUsername(user).orElseThrow();
        List<Session> sessions = sessionRepo.findByOrganizer(organizer).orElseThrow();
        List<GetSessionSingleResponse> sessionResponses = new ArrayList<>();
        for (Session session : sessions) {
            sessionResponses.add(
                    GetSessionSingleResponse.builder()
                            .sessionId(session.getId())
                            .slide(session.getSlide())
                            .build()
            );
        }
        return GetSessionResponse.builder()
                .sessions(sessionResponses)
                .build();
    }

    public SessionResponse editSession(String token, SessionEditRequest request) {
        var user = jwtService.extractBodyUsername(token);
        var session = sessionRepo.findById(request.getSessionId()).orElseThrow();
        if (!session.getOrganizer().getUsername().equals(user)) {
            throw new RuntimeException("You are not the organizer of this session");
        } else {
            session.setSlide(request.getSlide());
            sessionRepo.save(session);
        }
        return SessionResponse.builder()
                .isSuccessful(true)
                .id(session.getId())
                .build();
    }

    public StateResponse deleteSession(String token, Long sessionId) {
        var user = jwtService.extractBodyUsername(token);
        var session = sessionRepo.findById(sessionId).orElseThrow();
        if (!session.getOrganizer().getUsername().equals(user)) {
            throw new RuntimeException("You are not the organizer of this session");
        } else {
            sessionRepo.delete(session);
        }
        return StateResponse.builder()
                .isSuccessful(true)
                .build();
    }

    public QuestionResponse submitQuestion(String token, QuestionRequest request) {
        var user = jwtService.extractBodyUsername(token);
        var session = sessionRepo.findById(request.getSessionID()).orElseThrow();
        var participant = userRepo.findByUsername(user).orElseThrow();
        if (sharingRepo.findBySessionAndReceiver(session, participant).isEmpty()) {
            throw new RuntimeException("You are not a participant of this session");
        }
        var question = questionRepo.save(
                Question.builder()
                        .session(session)
                        .question(request.getQuestion())
                        .user(userRepo.findByUsername(user).orElseThrow())
                        .page(request.getSlideNum())
                        .build()
        );
        return QuestionResponse.builder()
                .id(question.getId())
                .question(question.getQuestion())
                .slideNum(question.getPage())
                .build();
    }

    public GetQuestionResponse getAllSessionQuestions(String token, Long sessionId) {
        var user = jwtService.extractBodyUsername(token);
        var session = sessionRepo.findById(sessionId).orElseThrow();
        if (!session.getOrganizer().getUsername().equals(user)) {
            throw new RuntimeException("You are not the organizer of this session");
        } else {
            List<Question> questions = questionRepo.findBySession(session).orElseThrow();
            return getGetQuestionResponse(questions);
        }
    }

    public GetQuestionResponse getAllUserQuestions(String token, Long sessionId) {
        var user = jwtService.extractBodyUsername(token);
        var userObj = userRepo.findByUsername(user).orElseThrow();
        var session = sessionRepo.findById(sessionId).orElseThrow();
        List<Question> questions = questionRepo.findBySessionAndUser(session, userObj).orElseThrow();
        return getGetQuestionResponse(questions);
    }

    private GetQuestionResponse getGetQuestionResponse(List<Question> questions) {
        List<QuestionResponse> questionResponses = new ArrayList<>();
        for (Question question : questions) {
            questionResponses.add(
                    QuestionResponse.builder()
                            .id(question.getId())
                            .question(question.getQuestion())
                            .slideNum(question.getPage())
                            .build()
            );
        }
        return GetQuestionResponse.builder()
                .questions(questionResponses)
                .build();
    }

    public StateResponse deleteQuestion(String token, Long questionId) {
        var user = jwtService.extractBodyUsername(token);
        var question = questionRepo.findById(questionId).orElseThrow();
        if (!question.getUser().getUsername().equals(user)&&!question.getSession().getOrganizer().getUsername().equals(user)) {
            throw new RuntimeException("You cannot delete this question");
        } else {
            questionRepo.delete(question);
        }
        return StateResponse.builder()
                .isSuccessful(true)
                .build();
    }
}
