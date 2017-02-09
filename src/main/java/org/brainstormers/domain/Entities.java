
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
    "dialoguesInfo",
    "input",
    "locale"
})
public class Entities {

    @JsonProperty("dialoguesInfo")
    private DialoguesInfo dialoguesInfo;
    @JsonProperty("input")
    private String input;
    @JsonProperty("locale")
    private String locale;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * 
     * @return
     *     The dialoguesInfo
     */
    @JsonProperty("dialoguesInfo")
    public DialoguesInfo getDialoguesInfo() {
        return dialoguesInfo;
    }

    /**
     * 
     * @param dialoguesInfo
     *     The dialoguesInfo
     */
    @JsonProperty("dialoguesInfo")
    public void setDialoguesInfo(DialoguesInfo dialoguesInfo) {
        this.dialoguesInfo = dialoguesInfo;
    }

    /**
     * 
     * @return
     *     The input
     */
    @JsonProperty("input")
    public String getInput() {
        return input;
    }

    /**
     * 
     * @param input
     *     The input
     */
    @JsonProperty("input")
    public void setInput(String input) {
        this.input = input;
    }

    /**
     * 
     * @return
     *     The locale
     */
    @JsonProperty("locale")
    public String getLocale() {
        return locale;
    }

    /**
     * 
     * @param locale
     *     The locale
     */
    @JsonProperty("locale")
    public void setLocale(String locale) {
        this.locale = locale;
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
