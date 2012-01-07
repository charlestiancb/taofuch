/*
 * Copyright 2011 Focus Technology, Co., Ltd. All rights reserved.
 */
package com.tfc.evolve.common.codec.encrypter;

/**
 * @author taofucheng
 */
public interface EncryptComponent {
    /**
     * 加密ID
     * 
     * @param id
     * @return
     */
    String encode(Long id);

    /**
     * @param id
     * @return
     */
    public String encodeImageID(Long id);

    /**
     * 解密ID
     * 
     * @param idEncrypt
     * @return
     */
    Long decode(String idEncrypt);

    /**
     * @param idEncrypt
     * @return
     */
    public Long decodeImageID(String idEncrypt);
}
