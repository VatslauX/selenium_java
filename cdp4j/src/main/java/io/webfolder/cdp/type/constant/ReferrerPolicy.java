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
package io.webfolder.cdp.type.constant;

import com.google.gson.annotations.SerializedName;

public enum ReferrerPolicy {
    @SerializedName("unsafe-url")
    UnsafeUrl("unsafe-url"),

    @SerializedName("no-referrer-when-downgrade")
    NoReferrerWhenDowngrade("no-referrer-when-downgrade"),

    @SerializedName("no-referrer")
    NoReferrer("no-referrer"),

    @SerializedName("origin")
    Origin("origin"),

    @SerializedName("origin-when-cross-origin")
    OriginWhenCrossOrigin("origin-when-cross-origin"),

    @SerializedName("same-origin")
    SameOrigin("same-origin"),

    @SerializedName("strict-origin")
    StrictOrigin("strict-origin"),

    @SerializedName("strict-origin-when-cross-origin")
    StrictOriginWhenCrossOrigin("strict-origin-when-cross-origin");

    public final String value;

    ReferrerPolicy(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
