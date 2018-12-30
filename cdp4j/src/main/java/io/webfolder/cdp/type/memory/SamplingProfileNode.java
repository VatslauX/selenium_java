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
package io.webfolder.cdp.type.memory;

import java.util.ArrayList;
import java.util.List;

/**
 * Heap profile sample
 */
public class SamplingProfileNode {
    private Double size;

    private Double total;

    private List<String> stack = new ArrayList<>();

    /**
     * Size of the sampled allocation.
     */
    public Double getSize() {
        return size;
    }

    /**
     * Size of the sampled allocation.
     */
    public void setSize(Double size) {
        this.size = size;
    }

    /**
     * Total bytes attributed to this sample.
     */
    public Double getTotal() {
        return total;
    }

    /**
     * Total bytes attributed to this sample.
     */
    public void setTotal(Double total) {
        this.total = total;
    }

    /**
     * Execution stack at the point of allocation.
     */
    public List<String> getStack() {
        return stack;
    }

    /**
     * Execution stack at the point of allocation.
     */
    public void setStack(List<String> stack) {
        this.stack = stack;
    }
}
