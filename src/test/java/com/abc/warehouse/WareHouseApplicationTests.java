package com.abc.warehouse;


import org.apache.commons.codec.binary.Base64;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class WareHouseApplicationTests {


    @Test
    public void test() throws Exception {
        String key ="UiPM9QWcQUxMO3GxdXJvCg==";
        String hello = EncryptUtil.aesEncrypt("hello", key);
        String s = EncryptUtil.aesDecrypt(hello, key);
        System.out.println(s);
    }

    @Test
    public void test1() throws Exception {
        RSAUtil.KeyStore keys = RSAUtil.createKeys();
        String publicKey = "MIGdMA0GCSqGSIb3DQEBAQUAA4GLADCBhwKBgQCdj9HDjC56KpP32cWdigXYGwU0vQBytyI2ObDAylF+aqZLeGcRoMw7Dmw8MiYwZgu74O+gQEMgGLvokfji9Jk9vtqKWdpUnwR1MnzxkavWbT5UGRa3muoBvw7s+GhqbWzF3buzbJ1RPSh8cLrfiZVqhjG5mPIQss/1aBOj6uUyJQIBAw==";
        String privateKey = "MIICdAIBADANBgkqhkiG9w0BAQEFAASCAl4wggJaAgEAAoGBAJ2P0cOMLnoqk/fZxZ2KBdgbBTS9AHK3IjY5sMDKUX5qpkt4ZxGgzDsObDwyJjBmC7vg76BAQyAYu+iR+OL0mT2+2opZ2lSfBHUyfPGRq9ZtPlQZFrea6gG/Duz4aGptbMXdu7NsnVE9KHxwut+JlWqGMbmY8hCyz/VoE6Pq5TIlAgEDAoGAGkKi9eyyabHDU/mg75cA+VnWM3TVaHPbCQmddXcNlRHGYelmgvAiCde8tLMGXWZXSfrSmrVghVl0psL+0H4Zigbx4U1kvNeDGNLd8jHB6I1IQQigmvbH1reLgIIE4THEVkIC5CI0jUrjzXHy0qM25F5wiSpPHnIsaSw4+xpL8/0CQQDlMn4notbZEpMP9AZO4Pb/Fj3I2nAGxOBqDyW/6kG83zjuK5OgkQ517UozBkvKGrYMG4AwAL6J/pYhS/I7IcljAkEAr/zEYdsQuN9NMVU+GD9sHnaQHHily3YZB7rELGDhhfOHY37G/tDzBeRhkgOj7DFaQ3MCaxabe8boPXHPEfuw1wJBAJjMVBpsjztht1/4BDSV+f9kKTCRoASDQEa0w9VG1n3qJfQdDRW2CaPzhsyu3TFnJAgSVXVV1Fv/DsDdTCdr25cCQHVTLZaSCyXqM3Y41BAqSBRPCr2lw9z5ZgUnLXLrQQP3r5ep2f8191lC67atF/LLkYJMrEdkZ6fZ8Cj2igv9II8CQH1AzwsNwUn/zfAqHnuSOPzKt+Hr+YnGhjez+Q/WHp9T/mku8b/SEMmo2e7kKcbZKRXA/0N16JXDh4MhGazQGNU=";
//        String publicKey = RSAUtil.getPublicKey();
//        String privateKey = RSAUtil.getPrivateKey();
        String bytes = RSAUtil.encryptByPublicKey("hello你好", publicKey);
        String bytes1 = RSAUtil.decryptByPrivateKey(bytes, privateKey);
        System.out.println(bytes1);
    }
}
