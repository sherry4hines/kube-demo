package com.s3pub.fdademo.fdademoservice.config;

import java.io.ByteArrayInputStream;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.interfaces.RSAPublicKey;
import java.util.Base64;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;

@Component
public class JwtTokenVerifier {
	  private static final Logger LOG = LoggerFactory.getLogger(JwtTokenVerifier.class);
 
    // please input your jwt token & public key here
    public static final String jwtToken = "";
    public static final String publicKey = "";
 
    public boolean isTokenValid(String token) {
        try {
            buildJWTVerifier().verify(token.replace("Bearer ", ""));
            // if token is valid no exception will be thrown
            return true;
        } catch (CertificateException | JWTVerificationException e) {
            //if CertificateException comes from buildJWTVerifier()
            LOG.error(e.getMessage());
            return false;
        } catch (Exception e) {
            // If any other exception comes
            LOG.error("General Exception: {}", e.getMessage());
            return false;
        }
    }
 
 
    private JWTVerifier buildJWTVerifier() throws CertificateException {
        var algo = Algorithm.RSA256(getRSAPublicKey(), null);
        return JWT.require(algo).build();
    }
 
    private RSAPublicKey getRSAPublicKey() throws CertificateException {
        var decode = Base64.getDecoder().decode(publicKey);
        var certificate = CertificateFactory.getInstance("X.509")
                .generateCertificate(new ByteArrayInputStream(decode));
        var publicKey = (RSAPublicKey) certificate.getPublicKey();
        return publicKey;
    }
}        	
        