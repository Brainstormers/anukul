
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
    "dialog",
    "dialog_id",
    "forced",
    "initiative",
    "match_id",
    "matchscore",
    "response_id"
})
public class DialoguesInfo {

    @JsonProperty("dialog")
    private String dialog;
    @JsonProperty("dialog_id")
    private String dialogId;
    @JsonProperty("forced")
    private Boolean forced;
    @JsonProperty("initiative")
    private Boolean initiative;
    @JsonProperty("match_id")
    private String matchId;
    @JsonProperty("matchscore")
    private String matchscore;
    @JsonProperty("response_id")
    private String responseId;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * 
     * @return
     *     The dialog
     */
    @JsonProperty("dialog")
    public String getDialog() {
        return dialog;
    }

    /**
     * 
     * @param dialog
     *     The dialog
     */
    @JsonProperty("dialog")
    public void setDialog(String dialog) {
        this.dialog = dialog;
    }

    /**
     * 
     * @return
     *     The dialogId
     */
    @JsonProperty("dialog_id")
    public String getDialogId() {
        return dialogId;
    }

    /**
     * 
     * @param dialogId
     *     The dialog_id
     */
    @JsonProperty("dialog_id")
    public void setDialogId(String dialogId) {
        this.dialogId = dialogId;
    }

    /**
     * 
     * @return
     *     The forced
     */
    @JsonProperty("forced")
    public Boolean getForced() {
        return forced;
    }

    /**
     * 
     * @param forced
     *     The forced
     */
    @JsonProperty("forced")
    public void setForced(Boolean forced) {
        this.forced = forced;
    }

    /**
     * 
     * @return
     *     The initiative
     */
    @JsonProperty("initiative")
    public Boolean getInitiative() {
        return initiative;
    }

    /**
     * 
     * @param initiative
     *     The initiative
     */
    @JsonProperty("initiative")
    public void setInitiative(Boolean initiative) {
        this.initiative = initiative;
    }

    /**
     * 
     * @return
     *     The matchId
     */
    @JsonProperty("match_id")
    public String getMatchId() {
        return matchId;
    }

    /**
     * 
     * @param matchId
     *     The match_id
     */
    @JsonProperty("match_id")
    public void setMatchId(String matchId) {
        this.matchId = matchId;
    }

    /**
     * 
     * @return
     *     The matchscore
     */
    @JsonProperty("matchscore")
    public String getMatchscore() {
        return matchscore;
    }

    /**
     * 
     * @param matchscore
     *     The matchscore
     */
    @JsonProperty("matchscore")
    public void setMatchscore(String matchscore) {
        this.matchscore = matchscore;
    }

    /**
     * 
     * @return
     *     The responseId
     */
    @JsonProperty("response_id")
    public String getResponseId() {
        return responseId;
    }

    /**
     * 
     * @param responseId
     *     The response_id
     */
    @JsonProperty("response_id")
    public void setResponseId(String responseId) {
        this.responseId = responseId;
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
