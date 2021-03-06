
package org.brainstormers.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
    "info",
    "output" 
})
public class Response {

    @JsonProperty("info")
    private Info info;
    @JsonProperty("output")
    private List<Output> output = new ArrayList<Output>();
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * 
     * @return
     *     The info
     */
    @JsonProperty("info")
    public Info getInfo() {
        return info;
    }

    /**
     * 
     * @param info
     *     The info
     */
    @JsonProperty("info")
    public void setInfo(Info info) {
        this.info = info;
    }

    /**
     * 
     * @return
     *     The output
     */
    @JsonProperty("output")
    public List<Output> getOutput() {
        return output;
    }

    /**
     * 
     * @param output
     *     The output
     */
    @JsonProperty("output")
    public void setOutput(List<Output> output) {
        this.output = output;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
