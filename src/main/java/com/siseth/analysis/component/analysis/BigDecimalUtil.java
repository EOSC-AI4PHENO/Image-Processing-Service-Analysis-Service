package com.siseth.analysis.component.analysis;

import java.math.BigDecimal;
import java.util.List;

public final class BigDecimalUtil {


    public static BigDecimal cast(Object val) {

        Object value = val instanceof List ?
                                ((List<Object>) val).get(0) :
                                val;

        if(value instanceof BigDecimal)
            return (BigDecimal) value;
        if(value instanceof Double)
            return BigDecimal.valueOf((Double) value);
        if(value instanceof Integer)
            return BigDecimal.valueOf((Integer) value);

        return null;
    }

}
