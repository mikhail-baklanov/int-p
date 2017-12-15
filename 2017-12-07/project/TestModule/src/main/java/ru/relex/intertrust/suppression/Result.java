package ru.relex.intertrust.suppression;

import java.util.List;

public class Result {
    private long parseTime;
    private long dirTime;
    private long findTime;
    private List<String> fileList;

    public long getParseTime() {
        return parseTime;
    }

    public long getDirTime() {
        return dirTime;
    }

    public long getFindTime() {
        return findTime;
    }

    public List<String> getFileList() {
        return fileList;
    }

    public void setParseTime(long parseTime) {
        this.parseTime = parseTime;
    }

    public void setDirTime(long dirTime) {
        this.dirTime = dirTime;
    }

    public void setFindTime(long findTime) {
        this.findTime = findTime;
    }

    public void setFileList(List<String> fileList) {
        this.fileList = fileList;
    }
}