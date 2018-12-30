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
package io.webfolder.cdp.event.storage;

import io.webfolder.cdp.annotation.Domain;
import io.webfolder.cdp.annotation.EventName;

/**
 * The origin's IndexedDB object store has been modified
 */
@Domain("Storage")
@EventName("indexedDBContentUpdated")
public class IndexedDBContentUpdated {
    private String origin;

    private String databaseName;

    private String objectStoreName;

    /**
     * Origin to update.
     */
    public String getOrigin() {
        return origin;
    }

    /**
     * Origin to update.
     */
    public void setOrigin(String origin) {
        this.origin = origin;
    }

    /**
     * Database to update.
     */
    public String getDatabaseName() {
        return databaseName;
    }

    /**
     * Database to update.
     */
    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }

    /**
     * ObjectStore to update.
     */
    public String getObjectStoreName() {
        return objectStoreName;
    }

    /**
     * ObjectStore to update.
     */
    public void setObjectStoreName(String objectStoreName) {
        this.objectStoreName = objectStoreName;
    }
}
