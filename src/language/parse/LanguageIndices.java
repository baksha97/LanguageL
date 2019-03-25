package language.parse;

public final class LanguageIndices {
    public static final int VARIABLE = 0;

    public final class Operator {
        public static final int VARIABLE_CHECK = 2;
        public static final int OPERATOR = 3;
        public static final int ONE_CHECK = 4;
    }

    public final class Conditional {
        public static final int VARIABLE_TO_CHECK = 1;
        public static final int POSSIBLE_NEW_LABEL = 5;
    }

    public final class Copy {
        public static final int VARIABLE_TO_COPY = 2;
    }

    public final class GoTo {
        public static final int NEW_LABEL = 1;
    }
}
