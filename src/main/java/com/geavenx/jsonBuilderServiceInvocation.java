package com.geavenx;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.l7tech.policy.assertion.ext.CustomAssertion;
import com.l7tech.policy.assertion.ext.CustomAssertionStatus;
import com.l7tech.policy.assertion.ext.ServiceInvocation;
import com.l7tech.policy.assertion.ext.message.CustomPolicyContext;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.logging.Logger;

public class jsonBuilderServiceInvocation extends ServiceInvocation {

    // This logger allows the assertion to generate log events that can be
    // captured by the gateway's log sinks.
    private static final Logger logger = Logger.getLogger(jsonBuilderServiceInvocation.class.getName());

    // This member references the assertion properties at runtime.
    // It is set in the setCustomAssertion() method below.
    private jsonBuilderCustomAssertion assertion;

    @Override
    // This method is called at runtime to pass the assertion properties.
    public void setCustomAssertion(CustomAssertion customAssertion) {

        super.setCustomAssertion(customAssertion);
        assert (customAssertion instanceof jsonBuilderCustomAssertion);
        assertion = (jsonBuilderCustomAssertion) customAssertion;
    }

    @Override
    public CustomAssertionStatus checkRequest(CustomPolicyContext customPolicyContext) {

        logger.fine("CUSTOM ASSERTION [JSON Builder]: Entering checkRequest");

        logger.fine("CUSTOM ASSERTION [JSON Builder]: Value of context_variable_key: " + assertion.getKey());
        logger.fine("CUSTOM ASSERTION [JSON Builder]: Value of context_variable_value: " + assertion.getValue());
        logger.fine("CUSTOM ASSERTION [JSON Builder]: Value of context_json_variable: " + assertion.OUTPUT_STRING_VARIABLE);

        String context_variable_key = customPolicyContext.expandVariable(assertion.getKey());
        String context_variable_value = customPolicyContext.expandVariable(assertion.getValue());
        Object context_json_variable = customPolicyContext.getVariable(assertion.OUTPUT_STRING_VARIABLE);

        if (context_json_variable == null) {

            logger.fine("CUSTOM ASSERTION [JSON Builder]: context_json_variable is null. Transforming to empty JSON object.");
            context_json_variable = "{}";

        }

        // Check type of context_json_variable
        if (context_json_variable instanceof String) {

            logger.fine("CUSTOM ASSERTION [JSON Builder]: Type of context_json_variable: String");

        } else if (context_json_variable instanceof JsonNode) {

            logger.warning("CUSTOM ASSERTION [JSON Builder]: Type of context_json_variable: JsonNode");
            return CustomAssertionStatus.FALSIFIED;

        } else {

            logger.warning("CUSTOM ASSERTION [JSON Builder]: Type of context_json_variable: Unknown");
            return CustomAssertionStatus.FALSIFIED;

        }

        String json_input_string = (String) context_json_variable;
        JsonNode json_node = parseJson(json_input_string);
        String final_json_string = addProperty(json_node, context_variable_key, context_variable_value);

        logger.fine("CUSTOM ASSERTION [JSON Builder]: Setting variable: " + json_input_string + " = " + final_json_string);

        customPolicyContext.setVariable(assertion.OUTPUT_STRING_VARIABLE, final_json_string);

        logger.fine("CUSTOM ASSERTION [JSON Builder]: Exiting checkRequest");

        return CustomAssertionStatus.NONE;
    }

    private static JsonNode parseJson(String json_string) {

        ObjectMapper object_mapper = new ObjectMapper();

        logger.fine("CUSTOM ASSERTION [JSON Builder]: parseJson method called");
        logger.fine("CUSTOM ASSERTION [JSON Builder]: Value of json_string: " + json_string);

        try {

            // Use ObjectMapper to parse the JSON string
            logger.fine("CUSTOM ASSERTION [JSON Builder]: Value of object_mapper.readTree(json_string): " + object_mapper.readTree(json_string));

            return object_mapper.readTree(json_string);

        } catch (Exception e) {

            // Handle parsing exception (e.g., log, return a default value)
            logger.warning("CUSTOM ASSERTION [JSON Builder]: Exception: " + e);

            return object_mapper.createObjectNode();

        }
    }

    private static String addProperty(JsonNode json_node, String key, String value) {
        // Create a new ObjectNode using ObjectMapper
        ObjectMapper object_mapper = new ObjectMapper();

        // Copy properties from the existing json_node to the new node
        JsonNode new_json_node = json_node.deepCopy();

        logger.fine("CUSTOM ASSERTION [JSON Builder]: addProperty method called");
        logger.fine("CUSTOM ASSERTION [JSON Builder]: Value of json_node: " + json_node);
        logger.fine("CUSTOM ASSERTION [JSON Builder]: Value of new_json_node: " + new_json_node);

        // Add the new property
        ((ObjectNode) new_json_node).put(key, value);

        // Convert the new ObjectNode to a JSON string
        String json_string = "{}";

        try {

            json_string = object_mapper.writeValueAsString(new_json_node);

        } catch (JsonProcessingException e) {

            logger.warning("CUSTOM ASSERTION [JSON Builder]: Exception: " + e);

        }

        logger.fine("CUSTOM ASSERTION [JSON Builder]: Value of json_string: " + json_string);
        return json_string;
    }
}
