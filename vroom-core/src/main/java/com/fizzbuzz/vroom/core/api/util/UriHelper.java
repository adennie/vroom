package com.fizzbuzz.vroom.core.api.util;

/*
 * Copyright (c) 2013 Fizz Buzz LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import com.fizzbuzz.vroom.core.exception.InvalidResourceUriException;
import com.google.common.collect.ImmutableMap;
import org.restlet.data.Status;
import org.restlet.resource.ResourceException;
import org.restlet.routing.Template;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class UriHelper {


    public static String formatUriTemplate(final String uriTemplate,
                                           final Map<String, String> uriTokenToValueMap) {
        String result = uriTemplate;

        // make an defensive immutable copy of the provided map
        ImmutableMap<String, String> iMap = ImmutableMap.copyOf(uriTokenToValueMap);

        for (Map.Entry<String, String> entry : iMap.entrySet()) {
            result = result.replace(tokenize(entry.getKey()), entry.getValue());
        }
        return result;
    }

    public static long getLongTokenValue(final String uri, final String uriTemplate, final String token) {
        long result;
        try {
            result = Long.parseLong((String) (getUriTokenValues(uri, uriTemplate).get(token)));
        } catch (Exception e) {
            throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, "invalid " + token + " URL component: " + uri);
        }
        return result;
    }



    protected static Map<String, Object> getUriTokenValues(final String uri, final String uriPathTemplate) {
        // get the path part of the URI and the path part of the URI template.  We ignore the server part
        // of the URI and the template because there are multiple valid ways to address the server and we don't want
        // those discrepancies to come into play.
        String path = null;
        String pathTemplate = null;
        try {
            path = new URL(uri).getPath();
        } catch (MalformedURLException e) {
            throw new InvalidResourceUriException(uri);
        }

        // extract the token values from the URI using the template as a guide
        Template template = new Template(uriPathTemplate);
        Map<String, Object> tokens = new HashMap<String, Object>();
        template.parse(path, tokens);
        return tokens;
    }

    public static String tokenize(final String tokenName) {
        return "{" + tokenName + "}";
    }
}
