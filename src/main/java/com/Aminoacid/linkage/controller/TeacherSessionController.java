package com.Aminoacid.linkage.controller;

import com.Aminoacid.linkage.dao.*;
import com.Aminoacid.linkage.service.SessionService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/teacher")
public class TeacherSessionController {

    private final SessionService sessionService;

    @PostMapping("/session")
    public SessionResponse createSession(@RequestHeader(name="Authorization") String token, @RequestBody CreateSessionRequest slideLink) {
        return sessionService.createSession(token, slideLink.getSlideLink());
    }

    @DeleteMapping("/session")
    public StateResponse deleteSession(@RequestHeader(name="Authorization") String token, @RequestBody SessionRequest sessionID) {
        return sessionService.deleteSession(token, sessionID.getSessionID());
    }

    @PostMapping("/session/add")
    public StateResponse addParticipant(@RequestHeader(name="Authorization") String token, @RequestBody ParticipantRequest request) {
        return sessionService.addParticipant(token, request);
    }

    @DeleteMapping("/session/remove")
    public StateResponse removeParticipant(@RequestHeader(name="Authorization") String token, @RequestBody ParticipantRequest request) {
        return sessionService.deleteParticipant(token, request);
    }

    @DeleteMapping("/session/question/remove/")
    public StateResponse removeQuestion(@RequestHeader(name="Authorization") String token, @RequestBody RemoveQuestionRequest questionID) {
        return sessionService.deleteQuestion(token, questionID.getQuestionID());
    }

    @PostMapping("/session/question")
    public GetQuestionResponse getQuestion(@RequestHeader(name="Authorization") String token, @RequestBody SessionRequest sessionID) {
        return sessionService.getAllSessionQuestions(token, sessionID.getSessionID());
    }
}
