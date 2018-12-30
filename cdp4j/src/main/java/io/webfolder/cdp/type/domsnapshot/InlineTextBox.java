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
package io.webfolder.cdp.type.domsnapshot;

import io.webfolder.cdp.type.dom.Rect;

/**
 * Details of post layout rendered text positions
 * The exact layout should not be regarded as
 * stable and may change between versions
 */
public class InlineTextBox {
    private Rect boundingBox;

    private Integer startCharacterIndex;

    private Integer numCharacters;

    /**
     * The bounding box in document coordinates. Note that scroll offset of the document is ignored.
     */
    public Rect getBoundingBox() {
        return boundingBox;
    }

    /**
     * The bounding box in document coordinates. Note that scroll offset of the document is ignored.
     */
    public void setBoundingBox(Rect boundingBox) {
        this.boundingBox = boundingBox;
    }

    /**
     * The starting index in characters, for this post layout textbox substring. Characters that
     * would be represented as a surrogate pair in UTF-16 have length 2.
     */
    public Integer getStartCharacterIndex() {
        return startCharacterIndex;
    }

    /**
     * The starting index in characters, for this post layout textbox substring. Characters that
     * would be represented as a surrogate pair in UTF-16 have length 2.
     */
    public void setStartCharacterIndex(Integer startCharacterIndex) {
        this.startCharacterIndex = startCharacterIndex;
    }

    /**
     * The number of characters in this post layout textbox substring. Characters that would be
     * represented as a surrogate pair in UTF-16 have length 2.
     */
    public Integer getNumCharacters() {
        return numCharacters;
    }

    /**
     * The number of characters in this post layout textbox substring. Characters that would be
     * represented as a surrogate pair in UTF-16 have length 2.
     */
    public void setNumCharacters(Integer numCharacters) {
        this.numCharacters = numCharacters;
    }
}
