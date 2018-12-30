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
package io.webfolder.cdp.command;

import io.webfolder.cdp.annotation.Domain;
import io.webfolder.cdp.annotation.Experimental;
import io.webfolder.cdp.type.storage.GetUsageAndQuotaResult;

@Experimental
@Domain("Storage")
public interface Storage {
    /**
     * Clears storage for origin.
     * 
     * @param origin Security origin.
     * @param storageTypes Comma separated origin names.
     */
    void clearDataForOrigin(String origin, String storageTypes);

    /**
     * Returns usage and quota in bytes.
     * 
     * @param origin Security origin.
     * 
     * @return GetUsageAndQuotaResult
     */
    GetUsageAndQuotaResult getUsageAndQuota(String origin);

    /**
     * Registers origin to be notified when an update occurs to its cache storage list.
     * 
     * @param origin Security origin.
     */
    void trackCacheStorageForOrigin(String origin);

    /**
     * Registers origin to be notified when an update occurs to its IndexedDB.
     * 
     * @param origin Security origin.
     */
    void trackIndexedDBForOrigin(String origin);

    /**
     * Unregisters origin from receiving notifications for cache storage.
     * 
     * @param origin Security origin.
     */
    void untrackCacheStorageForOrigin(String origin);

    /**
     * Unregisters origin from receiving notifications for IndexedDB.
     * 
     * @param origin Security origin.
     */
    void untrackIndexedDBForOrigin(String origin);
}
