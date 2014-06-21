package com.lda.result.parse;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import com.ss.language.model.data.DatabaseConfig;
import com.ss.language.model.data.EntitySql;
import com.ss.language.model.data.EntitySql.SqlType;

/*
 * 基础lda模型的解析，主要解析model-final.theta和
 * model-final.twords文件
 */
public class LDAparser {
	public int ntopics;
	public int ndocs;
	public int nwords;
	public String tableName;
	public String filepath;
	public List<word_topic_element> word_list = new HugeArrayList<word_topic_element>("word_list");
	public List<document_topic_element> doc_list = new HugeArrayList<document_topic_element>("doc_list");

	/*
	 * 初始化主题模型所需词汇，主题，文档各参数，从文件model-final.others中获得
	 */
	public void Lda_paremeter_inits(String filepath) throws NumberFormatException, IOException {
		this.filepath = filepath;
		BufferedReader infile = new BufferedReader(new FileReader(this.filepath + "/model-final.others"));
		String string;
		while ((string = infile.readLine()) != null) {
			System.out.println(string);
			if (string.indexOf("ntopics") != -1)
				this.ntopics = Integer.parseInt(string.substring(string.indexOf("=") + 1, string.length()));
			if (string.indexOf("ndocs") != -1)
				this.ndocs = Integer.parseInt(string.substring(string.indexOf("=") + 1, string.length()));
			if (string.indexOf("nwords") != -1)
				this.nwords = Integer.parseInt(string.substring(string.indexOf("=") + 1, string.length()));
		}
		infile.close();
	}

	/*
	 * 初始化数据库表格
	 */
	public void tables_inits() {

		String tableName = "existence_word_per_topic";
		// 先将存在的表删除
		String delSql = "DROP TABLE IF EXISTS " + tableName;
		DatabaseConfig.executeSql(new EntitySql().setSql(delSql).setType(SqlType.DELETE));
		String createSql = "create table "
				+ tableName
				+ "(`word` varchar(20) COLLATE utf8_bin NOT NULL,`phi_for_word` double ,`topic_id` varchar(20),`document_title` varchar(500) COLLATE utf8_bin NOT NULL,`is_topictoken` varchar(20) DEFAULT NULL)";
		DatabaseConfig.executeSql(new EntitySql().setSql(createSql).setType(SqlType.UPDATE));
		tableName = "topic_per_document";
		delSql = "DROP TABLE IF EXISTS " + tableName;
		DatabaseConfig.executeSql(new EntitySql().setSql(delSql).setType(SqlType.DELETE));
		createSql = "create table "
				+ tableName
				+ "(`document_title` varchar(500) COLLATE utf8_bin NOT NULL,`topic_id` varchar(20) COLLATE utf8_bin NOT NULL,`theat_for_document` double )";
		DatabaseConfig.executeSql(new EntitySql().setSql(createSql).setType(SqlType.UPDATE));
		tableName = "topic_per_word";// 包括在document中未出现词在某主题的概率
		/*
		 * 例如在三文件 0:1 1:1 2:1 3:1 4:1 5:1 6:1 7:0 1:1 8:1 9:1 4:1 5:1 8:0 10:0
		 * 0:0 1:0 2:0 9:0 4:0 5:0 10:0 中，文档1的标号为6的词被划分至1主题，6:1，而没有任何文档的
		 * 标号为6的词划分至主题0，即不存在6:0 但在model-final.phi文件中依然给出了编号为6的词在主题0上的概率
		 */

	}

	/*
	 * model-final.phi解析，主题-词汇矩阵
	 */
	public void model_word_phi_parse() throws IOException {
		BufferedReader infile = new BufferedReader(new FileReader(filepath + "/model-final.tassign"));
		String string;
		int document_line = 0;
		while ((string = infile.readLine()) != null) {

			String[] doc_each_line = string.split(" ");
			System.out.println(doc_each_line.length);
			for (int i = 0; i < doc_each_line.length; i++) {
				String[] word_per_topic = doc_each_line[i].split(":");
				word_topic_element wi = new word_topic_element();
				wi.word_id = Integer.parseInt(word_per_topic[0]);
				wi.topic_id = Integer.parseInt(word_per_topic[1]);
				wi.doc_id = document_line + 1;
				word_list.add(wi);
			}
			document_line++;
		}
		infile.close();
		if (document_line != ndocs)
			System.out.println("docs number not matching");
		else
			System.out.println("共解析" + document_line + "行文档");

		infile = new BufferedReader(new FileReader(filepath + "/model-final.phi"));
		int topic_count = 0;
		while ((string = infile.readLine()) != null) {
			String[] word_line = string.split(" ");
			for (int i = 0; i < word_line.length; i++)
				System.out.println("word line" + topic_count + "第" + i + "个元素是" + word_line[i]);
			if (word_line.length != nwords)
				System.out.println("words number not matching");
			else {
				System.out.println(word_list.size());
				for (int i = 0; i < word_list.size(); i++) {
					System.out.println("topic_count" + topic_count);
					System.out.println(word_list.get(i).word_id);
					if (word_list.get(i).topic_id == topic_count) {
						int num = word_list.get(i).word_id;
						System.out.println(num);
						System.out.println(word_line[num]);
						word_list.get(i).topic_value = Double.parseDouble(word_line[num]);
						System.out.println(word_list.get(i).topic_value);
					} else {
						System.out.println("topic " + topic_count + "与" + "word 所属主题" + word_list.get(i).topic_id
								+ "不匹配");
					}
					System.out.println("word " + word_list.get(i).word_id + "在 topic" + word_list.get(i).topic_id
							+ "上的概率值是" + word_list.get(i).topic_value);
				}
			}
			topic_count++;
		}
		infile.close();
		infile = new BufferedReader(new FileReader(filepath + "/wordmap.txt"));
		string = infile.readLine();
		while ((string = infile.readLine()) != null) {
			String[] word_map = string.split(" ");
			for (int i = 0; i < word_list.size(); i++) {
				int word_id = word_list.get(i).word_id;
				if (word_id == Integer.parseInt(word_map[0]))
					word_list.get(i).word_value = word_map[1];
			}
		}
		infile.close();
	}

	public void output_word_per_topic() {
		for (int i = 0; i < word_list.size(); i++) {
			// System.out.println(word_list.get(i).doc_id);
			String tableName = "existence_word_per_topic";
			String sql = "insert into " + tableName + "(word,phi_for_word,topic_id,document_title) values (" + "\'"
					+ word_list.get(i).word_value + "\'," + "\'" + word_list.get(i).topic_value + "\'," + "\'"
					+ word_list.get(i).topic_id + "\'," + "\'" + word_list.get(i).doc_id + "\'" + ")";
			System.out.println(sql);
			DatabaseConfig.executeSql(new EntitySql().setSql(sql).setType(SqlType.UPDATE));
			// System.out.println("word "+word_list.get(i).word_id+" to topic  "+word_list.get(i).topic_id+" is "+word_list.get(i).topic_value);
		}

	}

	/*
	 * 主题-文档矩阵解析，model-final.theta
	 */
	public void model_topic_document_theta_parse() throws IOException {
		BufferedReader infile = new BufferedReader(new FileReader(filepath + "/model-final.theta"));
		String string;
		int document_line = 0;
		while ((string = infile.readLine()) != null) {
			String[] document_per_topic = string.split(" ");
			for (int i = 0; i < document_per_topic.length; i++) {
				document_topic_element di = new document_topic_element();
				di.doc_id = document_line;
				di.topic_id = i;
				di.topic_value = Double.parseDouble(document_per_topic[i]);
				doc_list.add(di);

			}
			document_line++;
		}
		infile.close();
		infile = new BufferedReader(new FileReader(filepath + "/doc_ids.txt"));
		int doc_ids_count = 0;
		while ((string = infile.readLine()) != null) {
			for (int i = 0; i < doc_list.size(); i++) {
				int doc_id = doc_list.get(i).doc_id;
				if (doc_id == doc_ids_count)
					doc_list.get(i).doc_value = string;
			}
			doc_ids_count++;
		}
		infile.close();
	}

	public void output_topic_per_document() {
		for (int i = 0; i < doc_list.size(); i++) {
			String tableName = "topic_per_document";
			String sql = "insert into " + tableName + "(document_title,topic_id,theat_for_document) values (" + "\'"
					+ doc_list.get(i).doc_value + "\'," + "\'" + doc_list.get(i).topic_id + "\'," + "\'"
					+ doc_list.get(i).topic_value + "\'" + ")";
			System.out.println(sql);
			DatabaseConfig.executeSql(new EntitySql().setSql(sql).setType(SqlType.UPDATE));
			System.out.println("doc " + doc_list.get(i).doc_id + " to topic  " + doc_list.get(i).topic_id + " is "
					+ doc_list.get(i).topic_value);

		}

	}

	public static void main(String args[]) throws IOException {
		LDAparser l = new LDAparser();
		l.tables_inits();
		String filepath = "e://tmp//topic model";
		// 读入参数文件model-final.others
		l.Lda_paremeter_inits(filepath);
		System.out.println(l.ntopics);
		System.out.println(l.ndocs);
		System.out.println(l.nwords);
		l.model_word_phi_parse();
		l.output_word_per_topic();
		l.model_topic_document_theta_parse();
		l.output_topic_per_document();
	}
}
