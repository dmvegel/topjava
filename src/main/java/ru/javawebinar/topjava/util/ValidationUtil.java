package ru.javawebinar.topjava.util;

import org.springframework.core.NestedExceptionUtils;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.lang.NonNull;
import org.springframework.validation.BindingResult;
import ru.javawebinar.topjava.HasId;
import ru.javawebinar.topjava.util.exception.IllegalRequestDataException;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import javax.validation.*;
import java.util.Map;
import java.util.Set;

public class ValidationUtil {

    private static final Validator validator;
    public final static String USER_DUPLICATE_EMAIL_MSG_CODE = "user.duplicate";
    public final static String MEAL_DUPLICATE_DATETIME_MSG_CODE = "meal.duplicate";
    public final static String USERS_EMAIL_CONSTRAIN = "users_unique_email_idx";
    public final static String MEAL_DATETIME_CONSTRAIN = "meal_unique_user_datetime_idx";

    public final static Map<String, String> CONSTRAINS_I18N_MAP = Map.of(
            USERS_EMAIL_CONSTRAIN, USER_DUPLICATE_EMAIL_MSG_CODE,
            MEAL_DATETIME_CONSTRAIN, MEAL_DUPLICATE_DATETIME_MSG_CODE);

    static {
        //  From Javadoc: implementations are thread-safe and instances are typically cached and reused.
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        //  From Javadoc: implementations of this interface must be thread-safe
        validator = factory.getValidator();
    }

    private ValidationUtil() {
    }

    public static <T> void validate(T bean) {
        // https://alexkosarev.name/2018/07/30/bean-validation-api/
        Set<ConstraintViolation<T>> violations = validator.validate(bean);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
    }

    public static <T> T checkNotFound(T object, int id) {
        checkNotFound(object != null, id);
        return object;
    }

    public static void checkNotFound(boolean found, int id) {
        checkNotFound(found, "id=" + id);
    }

    public static <T> T checkNotFound(T object, String msg) {
        checkNotFound(object != null, msg);
        return object;
    }

    public static void checkNotFound(boolean found, String msg) {
        if (!found) {
            throw new NotFoundException("Not found entity with " + msg);
        }
    }

    public static void checkIsNew(HasId bean) {
        if (!bean.isNew()) {
            throw new IllegalRequestDataException(bean + " must be new (id=null)");
        }
    }

    public static void assureIdConsistent(HasId bean, int id) {
//      conservative when you reply, but accept liberally (http://stackoverflow.com/a/32728226/548473)
        if (bean.isNew()) {
            bean.setId(id);
        } else if (bean.id() != id) {
            throw new IllegalRequestDataException(bean + " must be with id=" + id);
        }
    }

    //  https://stackoverflow.com/a/65442410/548473
    @NonNull
    public static Throwable getRootCause(@NonNull Throwable t) {
        Throwable rootCause = NestedExceptionUtils.getRootCause(t);
        return rootCause != null ? rootCause : t;
    }

    public static void appendDuplicateEmailError(DataIntegrityViolationException e, BindingResult result) {
        Throwable root = getRootCause(e);
        if (root.getMessage() != null && root.getMessage().toLowerCase().contains(USERS_EMAIL_CONSTRAIN)) {
            result.rejectValue("email", USER_DUPLICATE_EMAIL_MSG_CODE);
        }
    }
}