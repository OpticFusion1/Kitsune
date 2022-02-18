package optic_fusion1.jre.util;

public final class Validate {

    private Validate() {
    }

    public static void isNotNull(Object object) {
        isNotNull(object, "The validated object is null");
    }

    public static void isNotNull(Object object, String message) {
        if (object != null) {
            return;
        }
        throw new IllegalArgumentException(message);
    }

}

