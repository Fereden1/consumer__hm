package farit.dev.service;

import farit.dev.dto.SignInRequest;
import farit.dev.dto.SignInResponse;

public interface SignInService {
    SignInResponse signIn(SignInRequest request);
}