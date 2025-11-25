package ru.javawebinar.topjava;

import org.junit.jupiter.api.extension.ConditionEvaluationResult;
import org.junit.jupiter.api.extension.ExecutionCondition;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;

public class ProfileCondition implements ExecutionCondition {
    @Override
    public ConditionEvaluationResult evaluateExecutionCondition(ExtensionContext context) {
        return Arrays.asList(SpringExtension.getApplicationContext(context).getEnvironment().getActiveProfiles())
                .contains(Profiles.DATAJPA)
                ? ConditionEvaluationResult.enabled("profile datajpa active")
                : ConditionEvaluationResult.disabled("profile datajpa not active");
    }
}
