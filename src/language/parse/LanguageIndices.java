package language.parse;

public final class LanguageIndices {
    public static final int VARIABLE = 0;
    public final class OPERATOR {
        public static final int VARIABLE_CHECK = 2;
        public static final int OPERATOR = 3;
        public static final int ONE_CHECK = 4;
    }

    public final class CONDITIONAL {
        public static final int VARIABLE_TO_CHECK = 1;
        public static final int POSSIBLE_NEW_STATE = 5;
    }

    public final class SET {
        public static final int VARIABLE_TO_COPY = 2;
    }

    public final class GOTO {
        public static final int NEW_STATE = 1;
    }
}
