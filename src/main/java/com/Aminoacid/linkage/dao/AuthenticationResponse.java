package com.Aminoacid.linkage.dao;

import com.Aminoacid.linkage.model.Roles;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class AuthenticationResponse {
    private String jwtToken;
    private Roles role;
}
