package com.siseth.analysis.component.file;

import java.io.File;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public abstract class FileReader {

    public File createFile(){
        return null;
    }

    protected boolean isExcluded(String name) {
        List<String> excludedField = Collections.singletonList("level");
        return name.startsWith("__") || excludedField.contains(name);
    }

    protected String tryCast(Object value){
        if(value instanceof String)
            return (String) value;
        if(value instanceof Long)
            return Long.toString((Long) value);
        if(value instanceof Integer)
            return Integer.toString((Integer) value);
        if(value instanceof Double)
            return Double.toString((Double) value);
        if(value instanceof OffsetDateTime)
            return ((OffsetDateTime) value).format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"));
        if(value instanceof Timestamp)
            return ((Timestamp) value).toLocalDateTime().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"));
        if(value instanceof LocalDate)
            return ((LocalDate)value).toString();
        if(value instanceof Date)
            return value.toString();
        if(value instanceof BigDecimal)
            return value.toString().replace(".",",");
        if(value instanceof Boolean)
            return ((Boolean)value) ? "1" : "0";
        if(value instanceof Character)
            return ((Character)value).toString();
        return "";
    }

}
