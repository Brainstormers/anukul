
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
    "apiVersion",
    "clientFeatures",
    "clientTime",
    "detectedLanguage",
    "emotions",
    "input",
    "isNewUser",
    "locale",
    "login",
    "responseTime"
})
public class Info {

    @JsonProperty("apiVersion")
    private String apiVersion;
    @JsonProperty("clientFeatures")
    private List<String> clientFeatures = new ArrayList<String>();
    @JsonProperty("clientTime")
    private String clientTime;
    @JsonProperty("detectedLanguage")
    private String detectedLanguage;
    @JsonProperty("emotions")
    private List<Object> emotions = new ArrayList<Object>();
    @JsonProperty("input")
    private String input;
    @JsonProperty("isNewUser")
    private Boolean isNewUser;
    @JsonProperty("locale")
    private String locale;
    @JsonProperty("login")
    private String login;
    @JsonProperty("responseTime")
    private Integer responseTime;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * 
     * @return
     *     The apiVersion
     */
    @JsonProperty("apiVersion")
    public String getApiVersion() {
        return apiVersion;
    }

    /**
     * 
     * @param apiVersion
     *     The apiVersion
     */
    @JsonProperty("apiVersion")
    public void setApiVersion(String apiVersion) {
        this.apiVersion = apiVersion;
    }

    /**
     * 
     * @return
     *     The clientFeatures
     */
    @JsonProperty("clientFeatures")
    public List<String> getClientFeatures() {
        return clientFeatures;
    }

    /**
     * 
     * @param clientFeatures
     *     The clientFeatures
     */
    @JsonProperty("clientFeatures")
    public void setClientFeatures(List<String> clientFeatures) {
        this.clientFeatures = clientFeatures;
    }

    /**
     * 
     * @return
     *     The clientTime
     */
    @JsonProperty("clientTime")
    public String getClientTime() {
        return clientTime;
    }

    /**
     * 
     * @param clientTime
     *     The clientTime
     */
    @JsonProperty("clientTime")
    public void setClientTime(String clientTime) {
        this.clientTime = clientTime;
    }

    /**
     * 
     * @return
     *     The detectedLanguage
     */
    @JsonProperty("detectedLanguage")
    public String getDetectedLanguage() {
        return detectedLanguage;
    }

    /**
     * 
     * @param detectedLanguage
     *     The detectedLanguage
     */
    @JsonProperty("detectedLanguage")
    public void setDetectedLanguage(String detectedLanguage) {
        this.detectedLanguage = detectedLanguage;
    }

    /**
     * 
     * @return
     *     The emotions
     */
    @JsonProperty("emotions")
    public List<Object> getEmotions() {
        return emotions;
    }

    /**
     * 
     * @param emotions
     *     The emotions
     */
    @JsonProperty("emotions")
    public void setEmotions(List<Object> emotions) {
        this.emotions = emotions;
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
     *     The isNewUser
     */
    @JsonProperty("isNewUser")
    public Boolean getIsNewUser() {
        return isNewUser;
    }

    /**
     * 
     * @param isNewUser
     *     The isNewUser
     */
    @JsonProperty("isNewUser")
    public void setIsNewUser(Boolean isNewUser) {
        this.isNewUser = isNewUser;
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

    /**
     * 
     * @return
     *     The login
     */
    @JsonProperty("login")
    public String getLogin() {
        return login;
    }

    /**
     * 
     * @param login
     *     The login
     */
    @JsonProperty("login")
    public void setLogin(String login) {
        this.login = login;
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

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
