package utils;

import jakarta.validation.*;
import java.util.Set;

public class ValidatorUtil {

    private static final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    private static final Validator validator = factory.getValidator();

    public static <T> void validate(T obj) {
        Set<ConstraintViolation<T>> violations = validator.validate(obj);

        if (!violations.isEmpty()) {
            StringBuilder sb = new StringBuilder("Erros de validação:\n");
            for (ConstraintViolation<T> v : violations) {
                sb.append("- ").append(v.getMessage()).append("\n");
            }
            throw new IllegalArgumentException(sb.toString());
        }
    }
}

