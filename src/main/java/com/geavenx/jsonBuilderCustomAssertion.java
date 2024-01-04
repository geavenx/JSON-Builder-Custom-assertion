package com.geavenx;

import com.l7tech.policy.assertion.SetsVariables;
import com.l7tech.policy.assertion.UsesVariables;
import com.l7tech.policy.assertion.ext.CustomAssertion;
import com.l7tech.policy.variable.ContextVariablesUtils;
import com.l7tech.policy.variable.VariableMetadata;

public class jsonBuilderCustomAssertion implements CustomAssertion, UsesVariables, SetsVariables {

    private String key, value;

    public final String OUTPUT_STRING_VARIABLE = "JSON_OUTPUT";

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String[] getVariablesUsed() {
        return ContextVariablesUtils.getReferencedNames(key + value);
    }

    @Override
    public String getName() {
        return "JSON Builder";
    }

    @Override
    public VariableMetadata[] getVariablesSet() {
        return new VariableMetadata[]{new VariableMetadata(this.OUTPUT_STRING_VARIABLE)};
    }
}
