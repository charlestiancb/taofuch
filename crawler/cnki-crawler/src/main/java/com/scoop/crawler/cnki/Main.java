package com.scoop.crawler.cnki;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

import com.scoop.crawler.cnki.organize.CnkiGenerParse;

public class Main {
	private static String index_filename = "E:/tmp/cnki/IndexJ.xls";

	public static void main(String[] args) throws IOException, BiffException {
		Workbook book = Workbook.getWorkbook(new File(index_filename));
		Sheet sheet = book.getSheet(0);
		int row = sheet.getRows();
		// System.out.println(row);
		for (int i = 1; i < row; i++) {
			CnkiGenerParse ct = new CnkiGenerParse();
			// 注意这里的取单元是先列后行
			ct.journal = sheet.getCell(0, i).getContents();// 期刊名
			System.out.println(ct.journal);
			ct.db = sheet.getCell(1, i).getContents();// 期刊对应编号
			ct.leftyear = Integer.valueOf(sheet.getCell(2, i).getContents());
			ct.rightyear = Integer.valueOf(sheet.getCell(3, i).getContents());
			ct.voltyear = Integer.valueOf(sheet.getCell(4, i).getContents());
			ct.leftvol = Integer.valueOf(sheet.getCell(5, i).getContents());
			ct.rightvol = Integer.valueOf(sheet.getCell(6, i).getContents());
			String textPath = "E:/tmp/cnki/" + ct.journal + ".txt";
			ct.rw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(textPath)));
			// 爬取对应期刊数据
			ct.JournalWholeParse(2001);
			if (ct.rw != null)
				ct.rw.close();

		}

		// 爬取结果写入文件，供数据库使用
		System.out.println("获取完成");

	}
}
