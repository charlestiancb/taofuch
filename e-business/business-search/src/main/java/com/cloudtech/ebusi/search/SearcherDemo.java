package com.cloudtech.ebusi.search;

import java.io.IOException;

import org.apache.lucene.index.Term;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;

import com.cloudtech.ebusi.crawler.parser.CompanyInfo;

public class SearcherDemo {

	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		TermQuery query = new TermQuery(new Term(CompanyInfo.INDEX_NAME, "广州"));
		TopDocs top = Searcher.indexSearher.search(query, 10);
		System.err.println(top.totalHits);
		ScoreDoc[] docs = top.scoreDocs;
		for (ScoreDoc doc : docs) {
			System.out.println(doc.score);
		}
	}

}
