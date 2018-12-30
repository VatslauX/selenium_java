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

import static io.webfolder.cdp.logger.CdpLoggerType.Console;
import static io.webfolder.cdp.logger.CdpLoggerType.Slf4j;

public class CdpLoggerFactory implements LoggerFactory {

    private final CdpLoggerType loggerType;

    private static CdpLogger NULL_LOGGER = new CdpLogger() {

        @Override
        public void info(String message, Object ...args) { }

        @Override
        public void debug(String message, Object ...args) { }

        @Override
        public void error(String message, Object ...args) { }

        @Override
        public void warn(String message, Object ...args) { }

        @Override
        public void error(String message, Throwable t) { }
    };

    public CdpLoggerFactory() {
        this(getDefaultLoggerType());
    }

    public CdpLoggerFactory(final CdpLoggerType loggerType) {
        this.loggerType = loggerType;
    }

    @Override
    public CdpLogger getLogger(String name) {
        try {
            switch (loggerType) {
                case Slf4j  : return new CdpSlf4jLogger(name);
                case Console: return new CdpConsoleLogger();
                case Log4j  : return new CdpLog4jLogger(name);
                default     : return NULL_LOGGER;
            }
        } catch (Throwable e) {
            return NULL_LOGGER;
        }
    }

    public static CdpLoggerType getDefaultLoggerType() {
        CdpLoggerType cdpLoggerType = Console;
        try {
            CdpLoggerFactory.class.getClassLoader().loadClass("org.slf4j.Logger");
            cdpLoggerType = Slf4j;
        } catch (ClassNotFoundException e) {
            // ignore
        }
        return cdpLoggerType;
    }
}
