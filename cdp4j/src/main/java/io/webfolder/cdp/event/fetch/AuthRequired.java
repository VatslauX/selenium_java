/**
 * cdp4j Commercial License
 *
 * Copyright 2017, 2018 WebFolder OÜ
 *
 * Permission  is hereby  granted,  to "____" obtaining  a  copy of  this software  and
 * associated  documentation files  (the "Software"), to deal in  the Software  without
 * restriction, including without limitation  the rights  to use, copy, modify,  merge,
 * publish, distribute  and sublicense  of the Software,  and to permit persons to whom
 * the Software is furnished to do so, subject to the following conditions:
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR  IMPLIED,
 * INCLUDING  BUT NOT  LIMITED  TO THE  WARRANTIES  OF  MERCHANTABILITY, FITNESS  FOR A
 * PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL  THE AUTHORS  OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF
 * CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE
 * OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package io.webfolder.cdp.event.fetch;

import io.webfolder.cdp.annotation.Domain;
import io.webfolder.cdp.annotation.EventName;
import io.webfolder.cdp.type.fetch.AuthChallenge;
import io.webfolder.cdp.type.network.Request;
import io.webfolder.cdp.type.network.ResourceType;

/**
 * Issued when the domain is enabled with handleAuthRequests set to true
 * The request is paused until client responds with continueWithAuth
 */
@Domain("Fetch")
@EventName("authRequired")
public class AuthRequired {
    private String requestId;

    private Request request;

    private String frameId;

    private ResourceType resourceType;

    private AuthChallenge authChallenge;

    /**
     * Each request the page makes will have a unique id.
     */
    public String getRequestId() {
        return requestId;
    }

    /**
     * Each request the page makes will have a unique id.
     */
    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    /**
     * The details of the request.
     */
    public Request getRequest() {
        return request;
    }

    /**
     * The details of the request.
     */
    public void setRequest(Request request) {
        this.request = request;
    }

    /**
     * The id of the frame that initiated the request.
     */
    public String getFrameId() {
        return frameId;
    }

    /**
     * The id of the frame that initiated the request.
     */
    public void setFrameId(String frameId) {
        this.frameId = frameId;
    }

    /**
     * How the requested resource will be used.
     */
    public ResourceType getResourceType() {
        return resourceType;
    }

    /**
     * How the requested resource will be used.
     */
    public void setResourceType(ResourceType resourceType) {
        this.resourceType = resourceType;
    }

    /**
     * Details of the Authorization Challenge encountered.
     * If this is set, client should respond with continueRequest that
     * contains AuthChallengeResponse.
     */
    public AuthChallenge getAuthChallenge() {
        return authChallenge;
    }

    /**
     * Details of the Authorization Challenge encountered.
     * If this is set, client should respond with continueRequest that
     * contains AuthChallengeResponse.
     */
    public void setAuthChallenge(AuthChallenge authChallenge) {
        this.authChallenge = authChallenge;
    }
}
