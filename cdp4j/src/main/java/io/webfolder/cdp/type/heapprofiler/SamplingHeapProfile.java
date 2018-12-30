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
package io.webfolder.cdp.type.heapprofiler;

import java.util.ArrayList;
import java.util.List;

/**
 * Sampling profile
 */
public class SamplingHeapProfile {
    private SamplingHeapProfileNode head;

    private List<SamplingHeapProfileSample> samples = new ArrayList<>();

    public SamplingHeapProfileNode getHead() {
        return head;
    }

    public void setHead(SamplingHeapProfileNode head) {
        this.head = head;
    }

    public List<SamplingHeapProfileSample> getSamples() {
        return samples;
    }

    public void setSamples(List<SamplingHeapProfileSample> samples) {
        this.samples = samples;
    }
}
