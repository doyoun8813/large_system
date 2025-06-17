package kuke.board.articleread.learnig;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

public class LongToDoubleTest {
    @Test
    void longToDoubleTets() {
        long longValue = 111_111_111_111_111_111L;
        System.out.println("longValue = " + longValue);
        double doubleValue = longValue;
        System.out.println("doubleValue = " + new BigDecimal(doubleValue).toString());
        long longValue2 = (long) doubleValue;
        System.out.println("longValue2 = " + longValue2);

        Long longVal = 111_111_111_111_111_111L;
        System.out.println("longVal = " + longVal);
        Double doubleVal = Double.valueOf(longVal);
        System.out.println("doubleVal = " + new BigDecimal(doubleVal).toString());
        Long longVal2 = doubleVal.longValue();
        System.out.println("longVal2 = " + longVal2);
    }
}
