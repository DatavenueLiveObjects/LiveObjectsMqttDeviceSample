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

import com.google.gson.annotations.SerializedName;

import java.util.HashMap;

@SuppressWarnings({"WeakerAccess", "unused"})
public class LoResource {
    /**
     * Resource declaration
     */
    public static class Resource {
        public Resource(String v, HashMap<String, Object> m) {
            this.v = v;
            this.m = m;
        }
        public String v;    // currently deployed version of this resource
        public HashMap<String, Object> m;    // (JSON object) (optional) metadata associated with this resource, useful to resource update
    }

    /**
     * Resource update request
     */
    public static class LoResourceUpdate {
        public String                  id;    // identifier of resource to update
        public String                  old;   // current resource version
        @SerializedName("new")
        public String                  new_;  // new resource version, to download and apply
        public HashMap<String, String> m;     // update details
        public Long                    cid;   // Correlation ID
    }

    /**
     * Resource update response
     */
    public enum RESPONSE_CODE {
        OK,                   // the update is accepted and will start
        UNKNOWN_RESOURCE,     // the update is refused, because the resource (identifier) is unsupported by the device
        WRONG_SOURCE_VERSION, // the device is no longer in the "current" (old) resource version specified in the resource update request
        WRONG_TARGET_VERSION, // the device doesn’t support the "target" (new) resource version specified in the resource update request
        INVALID_RESOURCE,     // the requested new resource version has incorrect version format or metadata
        NOT_AUTHORIZED,       // the device refuses to update the targeted resource (ex: bad timing, "read-only" resource, etc.)
        INTERNAL_ERROR        // an error occurred on the device, preventing for the requested resource update
    }
    public static class LoResourceResponse {
        public LoResourceResponse(RESPONSE_CODE res, Long cid) {
            this.res = res;
            this.cid = cid;
        }

        public RESPONSE_CODE res;
        public Long cid;
    }

    /**
     * Resource update response error details
     */
    public static class LoResourceResponseError {
        public LoResourceResponseError(String errorCode, String errorDetails) {
            this.errorCode = errorCode;
            this.errorDetails = errorDetails;
        }

        public String errorCode;
        public String errorDetails;
    }

    public HashMap<String, Resource> rsc = new HashMap<>();   // key = resource identifier
}