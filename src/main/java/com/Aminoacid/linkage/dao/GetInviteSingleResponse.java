package com.Aminoacid.linkage.dao;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetInviteSingleResponse {
    private Long sessionId;
    private String username;
}
