package com.cloudtech.ebusi.search;

import java.io.IOException;

import org.apache.lucene.index.IndexReader;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.store.FSDirectory;

import com.cloudtech.ebusi.index.CompanyIndexer;

public class CompanySearcher {
	private static IndexSearcher is = null;
	static {
		try {
			is = new IndexSearcher(IndexReader.open(FSDirectory.open(CompanyIndexer.indexDir)));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
