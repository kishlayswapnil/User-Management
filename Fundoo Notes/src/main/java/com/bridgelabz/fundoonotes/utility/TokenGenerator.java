package com.bridgelabz.fundoonotes.utility;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.Verification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class TokenGenerator {

    @Autowired
    private Environment environment;
    private static String TOKEN_SECRET;

    //Default constructor has been initialized.
    public TokenGenerator() {

    }

    public String generateUserToken(int id) {
        TOKEN_SECRET =  environment.getProperty("10");
        Algorithm algorithm = Algorithm.HMAC256(TOKEN_SECRET);
        String token = JWT.create().withClaim("ID", id).sign(algorithm);
        return token;
    }

    public int retrieveIdFromToken(String token) {
        TOKEN_SECRET =  environment.getProperty("10");
        Verification verification;
        verification = JWT.require(Algorithm.HMAC256(TOKEN_SECRET));
        JWTVerifier jwtverifier = verification.build();
        DecodedJWT decodedjwt = jwtverifier.verify(token);
        Claim claim = decodedjwt.getClaim("ID");
        int id = claim.asInt();
        return id;
    }
}