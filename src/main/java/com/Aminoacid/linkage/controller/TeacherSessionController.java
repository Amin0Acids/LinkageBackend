package com.Aminoacid.linkage.controller;

import com.Aminoacid.linkage.dao.GetQuestionResponse;
import com.Aminoacid.linkage.dao.ParticipantRequest;
import com.Aminoacid.linkage.dao.SessionResponse;
import com.Aminoacid.linkage.dao.StateResponse;
import com.Aminoacid.linkage.service.SessionService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/teacher")
public class TeacherSessionController {

    private final SessionService sessionService;

    @PostMapping("/session")
    public SessionResponse createSession(@RequestHeader(name="Authorization") String token, @RequestBody String slideLink) {
        return sessionService.createSession(token, slideLink);
    }

    @DeleteMapping("/session")
    public StateResponse deleteSession(@RequestHeader(name="Authorization") String token, @RequestBody Long sessionID) {
        return sessionService.deleteSession(token, sessionID);
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
    public StateResponse removeQuestion(@RequestHeader(name="Authorization") String token, @RequestBody Long questionID) {
        return sessionService.deleteQuestion(token, questionID);
    }

    @GetMapping("/session/question")
    public GetQuestionResponse getQuestion(@RequestHeader(name="Authorization") String token, @RequestBody Long sessionID) {
        return sessionService.getAllSessionQuestions(token, sessionID);
    }
}
