package com.service.microjc.Activity.App.Utils.security;


import com.service.microjc.stType.SecurityContent;

/*
 * RSA和AES结合使用
 *
 * */
public class AesRsa {

    /*服务端公私钥 RSA 1024  */
    static String severPubKey = "MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBAM2xhIF84m+DLjaoa19Lw3Tvr8szocIM2UueRbFFEStISh2jZVMaTNwWuIK0BD+QfUBkyOofTJfzre6Npw/p9DMCAwEAAQ==";
    static String serverPriKey = "MIIBVQIBADANBgkqhkiG9w0BAQEFAASCAT8wggE7AgEAAkEAzbGEgXzib4MuNqhrX0vDdO+vyzOhwgzZS55FsUURK0hKHaNlUxpM3Ba4grQEP5B9QGTI6h9Ml/Ot7o2nD+n0MwIDAQABAkBL40mCyIN2Jsy/B6YNk5M6KlAORS9Ru5HTz0HhJB0wfSLQu/T7Ugi+iPm79KK/QsmOpJxFWbTORCXy++63nJqZAiEA66l/fQhpZH3b+bLpeTNN91j+6wKevHaZrUsedUiZhU8CIQDfcetUaREmMbjElSx7hwTBn+MGYanPoACLc80x+VZx3QIhAKgAmQuI8KGcfDLh0kH+SknYnzrpDkLrBhmdoOQZ0qBPAiBnhWTs/5yFYIXk9ePc+yn19vh8bWT6H3wBH86Sd51YNQIhAI2/VKjdzy7oc+5kG0qNFcWT3f6cj+Q9TFWVSQhDbdKs";

    /* 客户端公私钥 RSA 2048*/
    static String clientPubKey = "MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBAMfHSNINxm4CekR8EIHcq9TwZBizj0CM24LBvgxpzZbkgSKLk4vUaAJ5xvY2BPkPi4bYTSD0LHcAoCiwt/SA7eECAwEAAQ==";
    static String clientPriKey = "MIIBUwIBADANBgkqhkiG9w0BAQEFAASCAT0wggE5AgEAAkEAx8dI0g3GbgJ6RHwQgdyr1PBkGLOPQIzbgsG+DGnNluSBIouTi9RoAnnG9jYE+Q+LhthNIPQsdwCgKLC39IDt4QIDAQABAkAZdawyGM9Q9b/fOnBgHF9Jo9kdzMRU1Z6j60ztCA7LS9oDAle7A4ISIrfjBJG6r51AZNinN4rd19VOI05jCn4BAiEA5dymfA87Czej0hCKOBF2d5CInrjwpM7hfFcK+SrNi/UCIQDefuVAw12e04Q9Tb/DHnvmWkr9fB11WJcyWTrwncfyvQIgXhwIzaIuulj9aXP8IxiDOFCkntd5LwDEsPOyOnGb1kECIHti88/OiASkWBmHHGkgZqWAPUYFpnCFCJQrV7xIQ7IhAiBwwJVhsTP1Xzq9hmnSGC+owUzKuVYf0mI1COgdYeFHcw==";


    /**
     * 双向认证
     */

    /*
     * 客户端接收来自服务器的加密数据
     * */
    public static String serverToClient(SecurityContent securityContent) throws Exception {
        byte[] c = Base64.decode(securityContent.getAesKey());
        /*3.客户端接收到数据后，先用服务端提供的公钥对加密过的AES密码进行解密*/
        byte[] decryptServerAesKey = RSAUtils.decryptByPublicKey(c, severPubKey);
        /*4.使用解密后的aeskey对加密数据进行解密*/
        String decryptServerData = AESUtils.decrypt(securityContent.getAesContent(), new String(decryptServerAesKey));

        return decryptServerData;
    }

    /*
     * 客户端给服务端传数据
     * */
    public static SecurityContent clientToServer(String clientData) throws Exception {
        SecurityContent securityContent = new SecurityContent();
        /*客户端生成的AES随机key*/
        String serverRandomKey = AESUtils.getRandomKey(16);
        /*1.客户端将要传输的明文数据用AES加密*/
        String serverAesEncryptData = AESUtils.encrypt(clientData, serverRandomKey);
        /*2.客户端使用客户端的私钥对AES的密码进行加密*/
        byte[] serverEncryptAesAKey = RSAUtils.encryptByPublicKey(serverRandomKey.getBytes(), clientPubKey);

        securityContent.setAesKey(Base64.encode(serverEncryptAesAKey));
        securityContent.setAesContent(serverAesEncryptData);

        return securityContent;


    }

}


