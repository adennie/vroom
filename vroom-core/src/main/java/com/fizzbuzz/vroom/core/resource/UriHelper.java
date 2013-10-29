package com.fizzbuzz.vroom.core.resource;

import com.fizzbuzz.vroom.core.exception.InvalidResourceUriException;
import com.google.common.collect.ImmutableMap;
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
        long result = Long.parseLong((String) (getUriTokenValues(uri, uriTemplate).get(token)));
        return result;
    }

    protected static Map<String, Object> getUriTokenValues(final String uri, final String uriTemplate) {
        // get the path part of the URI and the path part of the URI template.  We ignore the server part
        // of the URI and the template because there are multiple valid ways to address the server and we don't want
        // those discrepancies to come into play.
        String path = null;
        String pathTemplate = null;
        try {
            path = new URL(uri).getPath();
            pathTemplate = new URL(uriTemplate).getPath();
        } catch (MalformedURLException e) {
            throw new InvalidResourceUriException(uri);
        }

        // extract the token values from the URI using the template as a guide
        Template template = new Template(pathTemplate);
        Map<String, Object> tokens = new HashMap<String, Object>();
        template.parse(path, tokens);
        return tokens;
    }

    public static String tokenize(final String tokenName) {
        return "{" + tokenName + "}";
    }
}
