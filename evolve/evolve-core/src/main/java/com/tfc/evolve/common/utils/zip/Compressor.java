package com.tfc.evolve.common.utils.zip;

import java.io.File;
import java.io.IOException;

public abstract class Compressor {
    protected File mCompressedFile;

    public Compressor(File fl) {
        mCompressedFile = fl;
    }

    public void addFile(File file, boolean recursion, boolean pathInfo, int level) throws IOException {
        addFile(file, file.getParentFile(), recursion, pathInfo, true, level);
    }

    private void addFile(File file, File parent, boolean recursion, boolean pathInfo, boolean firstTime, int level)
            throws IOException {
        if (!file.isDirectory()) {
            String filePath = file.getAbsolutePath();
            String dirPath = parent.getAbsolutePath();
            String entryName = file.getName();
            if ((pathInfo) && (filePath.startsWith(dirPath))) {
                entryName = filePath.substring(dirPath.length());
            }
            setCompressionLevel(level);
            addFile(file, entryName.replaceAll("\\\\", "/"));
        }
        else if ((firstTime) || (recursion)) {
            File[] fileList = file.listFiles();
            for (int i = 0; i < fileList.length; i++) {
                addFile(fileList[i], parent, recursion, pathInfo, false, level);
            }
        }
    }

    public abstract void open() throws Exception;

    public abstract void addFile(File paramFile, String paramString) throws IOException;

    protected abstract void setCompressionLevel(int paramInt);

    public abstract void close() throws IOException;

    @Override
    public String toString() {
        return mCompressedFile.getAbsolutePath();
    }
}
