package org.zero.paymentservice.utils;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.zero.paymentservice.exception.AuthException;

@Component
@RequiredArgsConstructor
public class SignatureVerification {
    @Value("${app.security.token}")
    private String secretToken;

    public void verify(HttpServletRequest request, Object object) {
        var token = TokenExtractor.extract(request);
        if (token.isEmpty()) throw new AuthException("Not provided signature.");
        var compiledSignature = RequestBodyDecoder.compile(object, secretToken);
        System.out.println(token + " " + compiledSignature);
        if (!token.get().equals(compiledSignature)) throw new AuthException("Signature verification failed.");
    }
}
