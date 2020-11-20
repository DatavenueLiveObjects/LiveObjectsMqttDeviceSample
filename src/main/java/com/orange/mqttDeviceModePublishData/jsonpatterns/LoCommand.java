/*
 * Software Name : Live Objects Mqtt Device Sample
 * Version: 1.0
 * SPDX-FileCopyrightText: Copyright (c) 2019-2020 Orange Business Services
 * SPDX-License-Identifier: BSD-3-Clause
 * This software is distributed under the BSD-3-Clause,
 * the text of which is available at https://opensource.org/licenses/BSD-3-Clause
 * or see the "LICENCE" file for more details.
 * Software description: Sample application for Orange Datavenue Live Objects <a>https://liveobjects.orange-business.com</a>
 */

package com.orange.mqttDeviceModePublishData.jsonpatterns;

import java.util.Map;

@SuppressWarnings("WeakerAccess")
public class LoCommand {
    /**
     * Structure to answer to command requests
     */
    public static class LoCommandResponse {
        public LoCommandResponse(Map<String, Object> res, Long cid) {
            this.res = res;
            this.cid = cid;
        }

        public Map<String, Object> res;    // List of answers (keys and values are free) to the command
        public Long cid;                   // Correlation ID
    }

    public String req;               // command request
    public Map<String, Object> arg;  // command parameters
    public Long cid;                 // Correlation ID

    @Override public String toString() {
        return "LoCommand{" +
                "req='" + req + '\'' +
                ", arg=" + arg +
                ", cid=" + cid +
                '}';
    }
}