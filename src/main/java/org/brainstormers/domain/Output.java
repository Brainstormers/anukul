
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
    "actions",
    "entities",
    "handler",
    "responseTime",
    "results"
})
public class Output {

    @JsonProperty("actions")
    private Actions actions;
    @JsonProperty("entities")
    private Entities entities;
    @JsonProperty("handler")
    private String handler;
    @JsonProperty("responseTime")
    private Integer responseTime;
    @JsonProperty("results")
    private Results results;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * 
     * @return
     *     The actions
     */
    @JsonProperty("actions")
    public Actions getActions() {
        return actions;
    }

    /**
     * 
     * @param actions
     *     The actions
     */
    @JsonProperty("actions")
    public void setActions(Actions actions) {
        this.actions = actions;
    }

    /**
     * 
     * @return
     *     The entities
     */
    @JsonProperty("entities")
    public Entities getEntities() {
        return entities;
    }

    /**
     * 
     * @param entities
     *     The entities
     */
    @JsonProperty("entities")
    public void setEntities(Entities entities) {
        this.entities = entities;
    }

    /**
     * 
     * @return
     *     The handler
     */
    @JsonProperty("handler")
    public String getHandler() {
        return handler;
    }

    /**
     * 
     * @param handler
     *     The handler
     */
    @JsonProperty("handler")
    public void setHandler(String handler) {
        this.handler = handler;
    }

    /**
     * 
     * @return
     *     The responseTime
     */
    @JsonProperty("responseTime")
    public Integer getResponseTime() {
        return responseTime;
    }

    /**
     * 
     * @param responseTime
     *     The responseTime
     */
    @JsonProperty("responseTime")
    public void setResponseTime(Integer responseTime) {
        this.responseTime = responseTime;
    }

    /**
     * 
     * @return
     *     The results
     */
    @JsonProperty("results")
    public Results getResults() {
        return results;
    }

    /**
     * 
     * @param results
     *     The results
     */
    @JsonProperty("results")
    public void setResults(Results results) {
        this.results = results;
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
