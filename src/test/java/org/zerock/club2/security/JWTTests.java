package org.zerock.club2.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.zerock.club2.security.util.JWTUtil;


public class JWTTests {

    private JWTUtil jwtUtil;

    @BeforeEach
    public void testBefore(){

        System.out.println("testBefore......");
        jwtUtil = new JWTUtil();

    }

    @Test
    public void testEncode() throws  Exception{

        String email = "user90@zerock.org";

        String str = jwtUtil.generateToken(email);

        Thread.sleep(5000);

        String resultEmail = jwtUtil.validateAndExtract(str);

        System.out.println(resultEmail);

    }




}
