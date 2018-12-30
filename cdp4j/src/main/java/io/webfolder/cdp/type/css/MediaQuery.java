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
package io.webfolder.cdp.type.css;

import java.util.ArrayList;
import java.util.List;

/**
 * Media query descriptor
 */
public class MediaQuery {
    private List<MediaQueryExpression> expressions = new ArrayList<>();

    private Boolean active;

    /**
     * Array of media query expressions.
     */
    public List<MediaQueryExpression> getExpressions() {
        return expressions;
    }

    /**
     * Array of media query expressions.
     */
    public void setExpressions(List<MediaQueryExpression> expressions) {
        this.expressions = expressions;
    }

    /**
     * Whether the media query condition is satisfied.
     */
    public Boolean isActive() {
        return active;
    }

    /**
     * Whether the media query condition is satisfied.
     */
    public void setActive(Boolean active) {
        this.active = active;
    }
}
