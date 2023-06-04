package com.Aminoacid.linkage.dao;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuestionRequest {
    private Long sessionID;
    private String question;
    private Byte slideNum;
}
