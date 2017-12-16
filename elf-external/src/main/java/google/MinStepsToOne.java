/*
 *  DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  Copyright (c) 1997-2011 Oracle and/or its affiliates. All rights reserved.
 *
 *  The contents of this file are subject to the terms of either the GNU
 *  General Public License Version 2 only ("GPL") or the Common Development
 *  and Distribution License("CDDL") (collectively, the "License").  You
 *  may not use this file except in compliance with the License.  You can
 *  obtain a copy of the License at
 *  https://glassfish.dev.java.net/public/CDDL+GPL_1_1.html
 *  or packager/legal/LICENSE.txt.  See the License for the specific
 *  language governing permissions and limitations under the License.
 *
 *  When distributing the software, include this License Header Notice in each
 *  file and include the License file at packager/legal/LICENSE.txt.
 *
 *  GPL Classpath Exception:
 *  Oracle designates this particular file as subject to the "Classpath"
 *  exception as provided by Oracle in the GPL Version 2 section of the License
 *  file that accompanied this code.
 *
 *  Modifications:
 *  If applicable, add the following below the License Header, with the fields
 *  enclosed by brackets [] replaced by your own identifying information:
 *   "Portions Copyright [year] [name of copyright owner]"
 *
 *  Contributor(s):
 *  If you wish your version of this file to be governed by only the CDDL or
 *  only the GPL Version 2, indicate your decision by adding "[Contributor]
 *  elects to include this software in this distribution under the [CDDL or GPL
 *  Version 2] license."  If you don't indicate a single choice of license, a
 *  recipient has the option to distribute your version of this file under
 *  either the CDDL, the GPL Version 2 or to extend the choice of license to
 *  its licensees as provided above.  However, if you add GPL Version 2 code
 *  and therefore, elected the GPL Version 2 license, then the option applies
 *  only if the new code is made subject to such option by the copyright
 *  holder.
 */
package google;

/**
 *
 * @author wnevins
 */
public class MinStepsToOne {
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
            System.out.println(5 + " -->> " + getMinSteps(5));
        for(int i = 1; i < 10; i++) {
            System.out.println(i + " -->> " + getMinSteps(i));
        }
    }

    static int getMinSteps(int n) {

        int dp[] = new int[n + 1];
        int i = 1;

        dp[1] = 0;  // trivial case

        while (++i <= n) {

            dp[i] = 1 + dp[i - 1];

            if (i % 2 == 0)
                dp[i] = min(dp[i], 1 + dp[i / 2]);

            if (i % 3 == 0)
                dp[i] = min(dp[i], 1 + dp[i / 3]);

        }

        return dp[n];

    }

    static int min(int i1, int i2) {
        return i1 >= i2 ? i1 : i2;
    }
}
