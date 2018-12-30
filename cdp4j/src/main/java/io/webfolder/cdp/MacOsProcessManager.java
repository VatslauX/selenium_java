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
package io.webfolder.cdp;

import static java.lang.Class.forName;
import static java.lang.String.valueOf;
import static java.util.concurrent.TimeUnit.SECONDS;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import io.webfolder.cdp.exception.CdpException;

public class MacOsProcessManager extends ProcessManager {

    private int pid;

    private String cdp4jId;

    @Override
    void setProcess(CdpProcess process) {
        try {
            Field pidField = process.getProcess().getClass().getDeclaredField("pid");
            pidField.setAccessible(true);
            this.pid = (int) pidField.get(process.getProcess());
        } catch (Throwable e) {
            throw new CdpException(e);
        }
        this.cdp4jId = process.getCdp4jProcessId();
    }

    @Override
    public boolean kill() {
        ProcessBuilder builder = new ProcessBuilder("ps", "auxww");
        boolean found = false;
        try {
            Process process = builder.start();
            boolean ok = process.waitFor(5, SECONDS);
            if ( ! ok ) {
                return false;
            }
            if ( process.exitValue() != 0 ) {
                return false;
            }
            try (BufferedReader scanner = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line = scanner.readLine();
                String processId = "cdp4jId=" + cdp4jId;
                String strPid = " " + valueOf(pid) + " ";
                while ( ( line = scanner.readLine() ) != null ) {
                    if (line.contains(processId) && line.contains(strPid)) {
                        found = true;
                        break;
                    }
                }
            }
        } catch (Throwable e) {
            return false;
        }
        if ( ! found ) {
            return false;
        }
        try {
            Class<?> clazz = forName("java.lang.UNIXProcess");
            Method destroyProcess = clazz.getDeclaredMethod("destroyProcess",
                                                                int.class,
                                                                boolean.class);
            destroyProcess.setAccessible(true);
            boolean force = false;
            destroyProcess.invoke(null, pid, force);
            return true;
        } catch (Throwable e) {
            return false;
        }
    }
}
