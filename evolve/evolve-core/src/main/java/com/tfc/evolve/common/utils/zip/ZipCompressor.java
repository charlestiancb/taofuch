package com.tfc.evolve.common.utils.zip;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipOutputStream;

public class ZipCompressor extends Compressor {
    private ZipOutputStream mZos;
    private String encodeing;

    public ZipCompressor(File zipfl, String encoding) {
        super(zipfl);
        encodeing = encoding;
    }

    @Override
    public void open() throws Exception {
        try {
            mZos = new ZipOutputStream(new FileOutputStream(mCompressedFile));
            mZos.setEncoding(encodeing);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    @Override
    protected void setCompressionLevel(int level) {
        mZos.setLevel(level);
    }

    @Override
    public void addFile(File newEntry, String name) throws IOException {
        if (newEntry.isDirectory()) {
            return;
        }
        ZipEntry ze = new ZipEntry(name);
        mZos.putNextEntry(ze);
        FileInputStream fis = new FileInputStream(newEntry);
        byte[] fdata = new byte[512];
        for (int readCount = 0; (readCount = fis.read(fdata)) != -1;) {
            mZos.write(fdata, 0, readCount);
        }
        fis.close();
        mZos.closeEntry();
    }

    @Override
    public void close() throws IOException {
        mZos.close();
    }
}
