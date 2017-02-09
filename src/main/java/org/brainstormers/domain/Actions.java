
package org.brainstormers.domain;

import java.util.HashMap;
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
    "custom",
    "say"
})
public class Actions {

    @JsonProperty("custom")
    private Custom custom;
    @JsonProperty("say")
    private Say say;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * 
     * @return
     *     The custom
     */
    @JsonProperty("custom")
    public Custom getCustom() {
        return custom;
    }

    /**
     * 
     * @param custom
     *     The custom
     */
    @JsonProperty("custom")
    public void setCustom(Custom custom) {
        this.custom = custom;
    }

    /**
     * 
     * @return
     *     The say
     */
    @JsonProperty("say")
    public Say getSay() {
        return say;
    }

    /**
     * 
     * @param say
     *     The say
     */
    @JsonProperty("say")
    public void setSay(Say say) {
        this.say = say;
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
