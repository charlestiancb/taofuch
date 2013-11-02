package com.scoop.crawler.cnki.export;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;

import au.com.bytecode.opencsv.CSVWriter;

import com.scoop.crawler.cnki.entity.RefStatistic;
import com.scoop.crawler.cnki.entity.Reference;
import com.scoop.crawler.cnki.entity.SearchRecord;

public class CsvExport extends Export {
	/** 是否已经写过数据，用于记录是否需要写标题 */
	private boolean hasWrited = false;
	private CSVWriter mainWriter;
	private CSVWriter refWriter;
	private String mainFile = "main.csv";
	private String mainRefFile = "main_ref.csv";

	public CsvExport(File dir, String encoding) {
		if (dir == null) {
			throw new RuntimeException("没有指定Csv输出文件夹!");
		}
		try {
			FileUtils.deleteDirectory(dir);
			FileUtils.forceMkdir(dir);
		} catch (IOException e) {
			throw new RuntimeException("没有指定Csv输出文件夹!");
		}
		String enc = StringUtils.defaultIfBlank(encoding, "GBK");
		try {
			mainWriter = new CSVWriter(new OutputStreamWriter(
					new FileOutputStream(new File(dir, mainFile)), enc));
			refWriter = new CSVWriter(new OutputStreamWriter(
					new FileOutputStream(new File(dir, mainRefFile)), enc));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void write(SearchRecord record) {
		List<RefStatistic> refs = record.getRefs();
		if (!hasWrited) {
			mainWriter.writeNext(record.getPropNames());
			refWriter.writeNext(Reference.getPropNames());
			hasWrited = true;
		}
		// 先写主文件
		mainWriter.writeNext(record.getPropValues());
		// 再将引用文件都写进去
		if (refs != null && refs.size() > 0) {
			for (RefStatistic rs : refs) {
				List<Reference> references = rs.getRefs();
				if (references != null && references.size() > 0) {
					for (Reference f : references) {
						f.setRelationId(record.getIndex());
						refWriter.writeNext(f.getPropValues());
					}
				}
			}
		}
		try {
			mainWriter.flush();
			refWriter.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void close() {
		try {
			if (mainWriter != null) {
				mainWriter.flush();
				mainWriter.close();
			}
			if (refWriter != null) {
				refWriter.flush();
				refWriter.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
