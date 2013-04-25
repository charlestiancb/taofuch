package com.ss.language.model.pipe;

import java.util.LinkedHashMap;
import java.util.Map;

import com.ss.language.model.utils.ProcessUtils;

public class PipeManager {
	private static Map<String, PipeNode> pipes = new LinkedHashMap<String, PipeNode>();

	public static void regist(PipeNode pipe) {
		if (pipe == null) {
			return;
		}
		PipeNode node = pipes.get(pipe.getName());
		if (node == null) {
			pipes.put(pipe.getName(), pipe);
		}
	}

	/**
	 * 开始运行管道中的各个节点。注意：如果运行中途出现问题而停止，下次再运行这个方法时，会接着上次的运行处理进行！
	 */
	public static void start() {
		for (String nodeName : pipes.keySet()) {
			PipeNode node = pipes.get(nodeName);
			if (node == null || ProcessUtils.hasProcessed(node.getName())) {
				continue;
			}
			node.process();
			ProcessUtils.recordProcess(node.getName());
		}
	}

	/**
	 * 开始运行管道中的各个节点。注意：如果运行中途出现问题而停止，下次再运行这个方法时，会从头开始处理！
	 */
	public static void clearAndstart() {
		ProcessUtils.clearProcess();
		start();
	}
}
