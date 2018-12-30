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
package io.webfolder.cdp.logger;

import static io.webfolder.cdp.logger.CdpLogLevel.Debug;
import static io.webfolder.cdp.logger.CdpLogLevel.Error;
import static io.webfolder.cdp.logger.CdpLogLevel.Info;
import static io.webfolder.cdp.logger.CdpLogLevel.Warn;
import static io.webfolder.cdp.logger.MessageFormatter.arrayFormat;

public class CdpConsoleLogger implements CdpLogger {

    private final CdpLogLevel level;

    public CdpConsoleLogger() {
        this(Info);
    }

    public CdpConsoleLogger(final CdpLogLevel level) {
        this.level = level;
    }

    @Override
    public void info(String message, Object... args) {
        if (Info.equals(level) ||
                Debug.equals(level)) {
            FormattingTuple tuple = arrayFormat(message, args);
            System.out.println("[INFO] " + tuple.getMessage());
        }
    }

    @Override
    public void debug(String message, Object... args) {
        if (Debug.equals(level)) {
            FormattingTuple tuple = arrayFormat(message, args);
            System.out.println("[DEBUG] " + tuple.getMessage());
        }
    }

    @Override
    public void warn(String message, Object... args) {
        if (Info.equals(level)     ||
                Warn.equals(level) ||
                Debug.equals(level)) {
            FormattingTuple tuple = arrayFormat(message, args);
            System.out.println("[WARN] " + tuple.getMessage());
        }
    }

    @Override
    public void error(String message, Object... args) {
        if (Info.equals(level)      ||
                Warn.equals(level)  ||
                Error.equals(level) ||
                Debug.equals(level)) {
            FormattingTuple tuple = arrayFormat(message, args);
            System.out.println("[ERROR] " + tuple.getMessage());
        }
    }

    @Override
    public void error(String message, Throwable t) {
        if (Info.equals(level)      ||
                Warn.equals(level)  ||
                Error.equals(level) ||
                Debug.equals(level)) {
            System.err.println("[ERROR] " + message);
            if ( t != null ) {
                t.printStackTrace();
            }
        }
    }
}
