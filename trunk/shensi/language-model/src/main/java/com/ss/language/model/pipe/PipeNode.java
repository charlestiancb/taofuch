package com.ss.language.model.pipe;

/**
 * 每个管道节点
 * 
 * @author taofucheng
 * 
 */
public abstract class PipeNode {
	/** 处理入口 */
	public abstract void process();

	public String getName() {
		return this.getClass().getName();
	}
}
