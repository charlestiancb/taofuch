package com.ss.language.model.pipe;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang.StringUtils;

import com.ss.language.model.utils.ProcessUtils;

public class PipeManager {
	private static Map<String, PipeNode> pipes = new LinkedHashMap<String, PipeNode>();
	private static ThreadLocal<QueryWord> qw = new InheritableThreadLocal<QueryWord>();

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

	public static QueryWord getCurrentQueryWord() {
		return qw.get();
	}

	/**
	 * 开始运行管道中的各个节点。注意：如果运行中途出现问题而停止，下次再运行这个方法时，会从头开始处理！
	 */
	public static void clearAndstart(QueryWord... query) {
		if (query != null && query.length > 0) {
			List<QueryWord> qws = new ArrayList<QueryWord>();
			for (QueryWord q : query) {
				if (q != null && StringUtils.isNotBlank(q.getWord())) {
					qw.set(q);
					ProcessUtils.clearProcess();
					start();
					qw.set(null);
					qws.add(q);
				}
			}
			qw.set(null);
			// 将所有统计的结果按照各个查询的权重重新计算最后的分值
			new LastCombineResultProcessor().process(qws);
		}
	}

	/**
	 * 查询信息
	 * 
	 * @author taofucheng
	 * 
	 */
	public static class QueryWord {
		private String word;
		private double weight = 1;

		public QueryWord(String word) {
			setWord(word);
			setWeight(1);
		}

		public QueryWord(String word, double d) {
			setWord(word);
			setWeight(d);
		}

		public String getWord() {
			return word;
		}

		public void setWord(String word) {
			this.word = StringUtils.trim(word);
		}

		public double getWeight() {
			return weight;
		}

		public void setWeight(double weight) {
			this.weight = weight;
		}

		public String getTableSuffix() {
			try {
				return Hex.encodeHexString(word.getBytes("UTF-8"));
			} catch (Exception e) {
				return word;
			}
		}
	}
}
