package com.tfc.evolve.common.exception;

/**
 * 系统错误类
 * 
 * @author geliang
 */
public class SystemErrorException extends RuntimeException {

    public SystemErrorException(String msg) {
        super(msg);
    }

    public SystemErrorException(Throwable e) {
        super(e);
    }

    public SystemErrorException(String msg, Throwable e) {
        super(msg, e);
    }
    private static final long serialVersionUID = 6863919340008867446L;

}
