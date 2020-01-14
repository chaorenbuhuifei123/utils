package mc.utils;

import mc.exception.ExpectedException;

public class ExtAssert {

    public static void notNull(Object object, String code, String message) throws ExpectedException {
        if (object == null) {
            throw new ExpectedException(code, message);
        }
    }

    public static void hasText(String text, String code, String message) throws ExpectedException {
        if (text == null || text.trim().length() == 0) {
            throw new ExpectedException(code, message);
        }
    }

    public static void isTrue(boolean expression, String code, String message) throws ExpectedException {
        if (!expression) {
            throw new ExpectedException(code, message);
        }
    }

}
