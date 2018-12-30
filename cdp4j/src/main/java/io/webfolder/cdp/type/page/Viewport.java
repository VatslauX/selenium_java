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
package io.webfolder.cdp.type.page;

/**
 * Viewport for capturing screenshot
 */
public class Viewport {
    private Double x;

    private Double y;

    private Double width;

    private Double height;

    private Double scale;

    /**
     * X offset in CSS pixels.
     */
    public Double getX() {
        return x;
    }

    /**
     * X offset in CSS pixels.
     */
    public void setX(Double x) {
        this.x = x;
    }

    /**
     * Y offset in CSS pixels
     */
    public Double getY() {
        return y;
    }

    /**
     * Y offset in CSS pixels
     */
    public void setY(Double y) {
        this.y = y;
    }

    /**
     * Rectangle width in CSS pixels
     */
    public Double getWidth() {
        return width;
    }

    /**
     * Rectangle width in CSS pixels
     */
    public void setWidth(Double width) {
        this.width = width;
    }

    /**
     * Rectangle height in CSS pixels
     */
    public Double getHeight() {
        return height;
    }

    /**
     * Rectangle height in CSS pixels
     */
    public void setHeight(Double height) {
        this.height = height;
    }

    /**
     * Page scale factor.
     */
    public Double getScale() {
        return scale;
    }

    /**
     * Page scale factor.
     */
    public void setScale(Double scale) {
        this.scale = scale;
    }
}
