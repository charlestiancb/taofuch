package org.wltea.analyzer.dic;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import junit.framework.TestCase;

import org.wltea.analyzer.dic.DictSegment;
import org.wltea.analyzer.dic.Hit;

/**
 * 
 * IK 中文分词  版本 5.0
 * IK Analyzer release 5.0
 * 
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * 源代码由林良益(linliangyi2005@gmail.com)提供
 * 版权声明 2012，乌龙茶工作室
 * provided by Linliangyi and copyright 2012 by Oolong studio
 * 
 */
public class DictSegmentTester extends TestCase {
	
	/*
	 * 分词器默认字典路径 
	 */
	public static final String PATH_DIC_MAIN = "/org/wltea/analyzer/dic/main2012.dic";
	public static final String PATH_DIC_SURNAME = "/org/wltea/analyzer/dic/surname.dic";
	public static final String PATH_DIC_QUANTIFIER = "/org/wltea/analyzer/dic/quantifier.dic";
	public static final String PATH_DIC_SUFFIX = "/org/wltea/analyzer/dic/suffix.dic";
	public static final String PATH_DIC_PREP = "/org/wltea/analyzer/dic/preposition.dic";
	public static final String PATH_DIC_STOP = "/stopword.dic";

	
	public void testMainDictMemoryConsume(){
        InputStream is = DictSegmentTester.class.getResourceAsStream(PATH_DIC_MAIN);
        System.out.println(new Date() + " before load dictionary");
        DictSegment _root_ = new DictSegment((char)0);
        try {
			Thread.sleep(20000);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        System.out.println(new Date() + " loading dictionary");
		try {
			String theWord = null;
			BufferedReader br = new BufferedReader(new InputStreamReader(is , "UTF-8"), 512);
			do {
				theWord = br.readLine();
				if (theWord != null) {
					_root_.fillSegment(theWord.toCharArray());
				}
			} while (theWord != null);
			System.out.println(new Date() + " after load dictionary");

	
		} catch (IOException ioe) {
			System.err.println("主词典库载入异常.");
			ioe.printStackTrace();
		}finally{
			try {
				if(is != null){
                    is.close();
                    is = null;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
        try {
			Thread.sleep(20000);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}		
	}	
	
	
	
	public void testDictSegmentSearch(){
        InputStream is = DictSegmentTester.class.getResourceAsStream(PATH_DIC_MAIN);
        System.out.println(new Date() + " before load dictionary");

        DictSegment _root_ = new DictSegment((char)0);
        List<String> allWords = new ArrayList<String>();
        
        System.out.println(new Date() + " loading dictionary");
		try {
			String theWord = null;
			BufferedReader br = new BufferedReader(new InputStreamReader(is , "UTF-8"), 512);
			do {
				theWord = br.readLine();
				if (theWord != null) {
					allWords.add(theWord.trim());
					_root_.fillSegment(theWord.trim().toCharArray());
				}
			} while (theWord != null);
			System.out.println(new Date() + " after load dictionary");

	
		} catch (IOException ioe) {
			System.err.println("主词典库载入异常.");
			ioe.printStackTrace();
		}finally{
			try {
				if(is != null){
                    is.close();
                    is = null;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
        try {
			Thread.sleep(3000);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		System.out.println(new Date() + " begin march");
		long begintime = System.currentTimeMillis();
		Hit hit = null;
		int umCount = 0;
		int mCount = 0;
		for(String word : allWords){
			char[] chars = word.toCharArray();
			hit = _root_.match(chars , 0, chars.length);
			if(hit.isUnmatch()){
				//System.out.println(word);
				umCount++;
			}else{
				mCount++;
				//System.out.println(mCount + " : " + word);
			}
		}
		System.out.println(new Date() + " finish march , cost " + (System.currentTimeMillis() - begintime ) + " millseconds");
		System.out.println("Total words : " + allWords.size() + " Match words : " + mCount + " Unmatch words : " + umCount);
	}
	
	public void testDisableDictSegment(){
        InputStream is = DictSegmentTester.class.getResourceAsStream(PATH_DIC_MAIN);
        System.out.println(new Date() + " before load dictionary");

        DictSegment _root_ = new DictSegment((char)0);
        List<String> allWords = new ArrayList<String>();
        
        System.out.println(new Date() + " loading dictionary");
		try {
			String theWord = null;
			BufferedReader br = new BufferedReader(new InputStreamReader(is , "UTF-8"), 512);
			do {
				theWord = br.readLine();
				if (theWord != null) {
					allWords.add(theWord.trim());
					_root_.fillSegment(theWord.trim().toCharArray());
				}
			} while (theWord != null);
			System.out.println(new Date() + " after load dictionary");

	
		} catch (IOException ioe) {
			System.err.println("主词典库载入异常.");
			ioe.printStackTrace();
		}finally{
			try {
				if(is != null){
                    is.close();
                    is = null;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
        try {
			Thread.sleep(3000);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		String testword = "中华人民共和国";
		Hit hit1 = _root_.match(testword.toCharArray());
		System.out.println("hit 1 = " + hit1.isMatch());
		
		
        try {
			Thread.sleep(1000);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		System.out.println("disable " + testword);
		_root_.disableSegment(testword.toCharArray());
		Hit hit2 = _root_.match(testword.toCharArray());
		System.out.println("hit 2 = " + hit2.isMatch());
		
		
        try {
			Thread.sleep(1000);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		System.out.println("enable " + testword);
		_root_.fillSegment(testword.toCharArray());
		Hit hit3 = _root_.match(testword.toCharArray());
		System.out.println("hit 3 = " + hit3.isMatch());
	}
	

		
}
