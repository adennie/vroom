package com.fizzbuzz.vroom.core.util;

/*
 * Copyright (c) 2014 Fizz Buzz LLC
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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.InflaterInputStream;

public class Strings {
    public static byte[] compress(final String s) throws IOException {
        byte[] result = s.getBytes();
        if (s != null && s.length() > 0) {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            DeflaterOutputStream deflaterOut = new DeflaterOutputStream(out);
            deflaterOut.write(s.getBytes());
            deflaterOut.close();
            result = out.toByteArray();
            Logger logger = LoggerFactory.getLogger(PackageLogger.TAG);
            logger.debug("Strings.compress: input size={}, compressed size={}", s.length(), result.length);
        }
        return result;
    }

    public static String decompress(final byte[] data) throws IOException {
        String result=null;
        if (data != null && data.length > 0) {
            InflaterInputStream inflaterIn = new InflaterInputStream(new ByteArrayInputStream(data));
            result = inputStreamToString(inflaterIn);
        }
        return result;
    }

    public static String inputStreamToString(InputStream inputStream) throws IOException {
        StringBuilder builder = new StringBuilder();
        InputStreamReader reader = new InputStreamReader(inputStream);
        char[] buffer = new char[4096];
        int length;
        while ((length = reader.read(buffer)) != -1) {
            builder.append(buffer, 0, length);
        }
        return builder.toString();
    }
}
