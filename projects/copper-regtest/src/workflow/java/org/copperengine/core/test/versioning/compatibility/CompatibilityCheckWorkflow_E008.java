/*
 * Copyright 2002-2015 SCOOP Software GmbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.copperengine.regtest.test.versioning.compatibility;

import java.io.Serializable;

import org.copperengine.core.Interrupt;
import org.copperengine.core.WaitMode;
import org.copperengine.core.WorkflowDescription;
import org.copperengine.core.persistent.PersistentWorkflow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * Incompatible change example E008
 * This class is a incompatible version of {@link CompatibilityCheckWorkflow_Base}. The following change(s) are applied:
 * Reordered local variables localValue3 and localIntValue of same type. The strange result do not lead to an exception, created by COPPER!
 *
 * @author sluyterman
 */
@WorkflowDescription(alias = CompatibilityCheckWorkflowDef.NAME, majorVersion = 1, minorVersion = 0, patchLevelVersion = 0)
public class CompatibilityCheckWorkflow_E008 extends PersistentWorkflow<Serializable> {

    private static final Logger logger = LoggerFactory.getLogger(CompatibilityCheckWorkflow_E008.class);

    private static final long serialVersionUID = 1L;

    private String aString;
    private String bString;

    @Override
    public void main() throws Interrupt {
        aString = "A";
        int localValue3 = 2;
        int localIntValue = 99;
        String localValue4 = null;
        directlyWaitingMethod(aString, localIntValue);
        bString = "B";
        localIntValue++;
        indirectlyWaitingMethod(bString, localIntValue);
        if (99 != localIntValue) {
            throw new org.copperengine.regtest.test.versioning.compatibility.UnexpectedValueException("Unexpected localIntValue=" + localIntValue + " (expected is 99).");
        }
    }

    protected void directlyWaitingMethod(String strValue, int intValue) throws Interrupt {
        neverWaitingMethod(strValue, intValue);
        this.wait(WaitMode.ALL, 500, Long.toHexString(System.currentTimeMillis()));
    }

    protected void indirectlyWaitingMethod(String strValue, int intValue) throws Interrupt {
        final Object localObject = 10867L;
        directlyWaitingMethod(strValue, intValue);
        logger.debug("{}", localObject);
    }

    protected void neverWaitingMethod(String strValue, int intValue) {
        logger.debug("strValue=" + strValue + ", intValue=" + intValue);
        anotherNeverWaitingMethod(strValue, intValue);
    }

    protected void anotherNeverWaitingMethod(String strValue, int intValue) {
        logger.debug("strValue=" + strValue + ", intValue=" + intValue);
    }

}
