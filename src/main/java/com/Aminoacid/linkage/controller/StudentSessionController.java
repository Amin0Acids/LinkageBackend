package com.Aminoacid.linkage.controller;

import com.Aminoacid.linkage.dao.GetSessionSingleResponse;
import com.Aminoacid.linkage.dao.QuestionRequest;
import com.Aminoacid.linkage.dao.QuestionResponse;
import com.Aminoacid.linkage.dao.SessionRequest;
import com.Aminoacid.linkage.service.SessionService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/student")
public class StudentSessionController {

    private final SessionService sessionService;

    @PostMapping("/session")
    public GetSessionSingleResponse getSession(@RequestHeader(name="Authorization") String token, @RequestBody SessionRequest sessionID) {
        return sessionService.getSession(token, sessionID.getSessionID());
    }

    @PostMapping("/question")
    public QuestionResponse question(@RequestHeader(name="Authorization") String token, @RequestBody QuestionRequest question) {
        return sessionService.submitQuestion(token, question);
    }
}
