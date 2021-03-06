/*
 * Copyright 2002-2017 SCOOP Software GmbH
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

package org.copperengine.management.model;

import java.io.Serializable;
import java.util.Date;

public class ErrorData implements Serializable {

    private static final long serialVersionUID = 1L;
    
    private Date errorTS;
    private String exceptionStackTrace;
    
    public ErrorData() {
    }

    public ErrorData(Date errorTS, String exceptionStackTrace) {
        this.errorTS = errorTS;
        this.exceptionStackTrace = exceptionStackTrace;
    }

    public Date getErrorTS() {
        return errorTS;
    }

    public void setErrorTS(Date errorTS) {
        this.errorTS = errorTS;
    }

    public String getExceptionStackTrace() {
        return exceptionStackTrace;
    }

    public void setExceptionStackTrace(String exceptionStackTrace) {
        this.exceptionStackTrace = exceptionStackTrace;
    }

    @Override
    public String toString() {
        return "ErrorData [errorTS=" + errorTS + ", exceptionStackTrace=" + exceptionStackTrace + "]";
    }
    
}
