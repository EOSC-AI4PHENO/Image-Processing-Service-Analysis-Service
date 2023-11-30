package com.siseth.analysis.component.file;

import lombok.NoArgsConstructor;
import lombok.SneakyThrows;

import java.io.File;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@NoArgsConstructor
public final class CSVCreator extends FileReader {

    private List<Map<String,Object>> data;

    private String separator = ",";

    public CSVCreator(List<Map<String,Object>> data) {
        this.data = data;
    }

    public CSVCreator(List<Map<String,Object>> data, String separator) {
        this.data = data;
        this.separator = separator;
    }


    @SneakyThrows
    public File createFile() {
        if(data.size() == 0)
            return null;

        Map<String,Object> header = data.get(0);

        File csvOutputFile = File.createTempFile("file", ".csv");
        csvOutputFile.deleteOnExit();

        try (PrintWriter pw = new PrintWriter(csvOutputFile)) {
            pw.println(createHeader(header));

            data.stream()
                    .map(this::createRowData)
                    .forEach(pw::println);
        }

        return csvOutputFile;
    }



    private String createHeader(Map<String,Object> header) {
        return header.keySet()
                .stream()
                .filter(x -> !isExcluded(x))
                .collect(Collectors.joining(this.separator));
    }

    private String createRowData(Map<String, Object> data) {
        return data.entrySet()
                .stream()
                .filter(x -> !isExcluded(x.getKey()))
                .map(Map.Entry::getValue)
                .map(x -> x != null ? x.toString() : "")
                .collect(Collectors.joining(this.separator));
    }

}
