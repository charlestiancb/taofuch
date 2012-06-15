package com.cloudtech.ebusi.lucene;

import java.io.IOException;
import java.io.Reader;

import org.apache.lucene.analysis.Tokenizer;

public class IctclasTokenizer extends Tokenizer {
	public IctclasTokenizer(Reader input) {
		super(input);
	}

	@Override
	public boolean incrementToken() throws IOException {
		// TODO Auto-generated method stub
		return false;
	}

}
